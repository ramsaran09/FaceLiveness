package com.muthuram.faceliveness.tensorHelper

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import android.util.Pair
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.Vector
import kotlin.math.abs
import kotlin.math.sqrt

class TFLiteObjectDetectionAPIModel : SimilarityClassifier {
    private var isModelQuantized = false

    private var inputSize = 0

    // Pre-allocated buffers.
    private val labels = Vector<String>()
    private var intValues: IntArray = intArrayOf()

    // outputLocations: array of shape [Batchsize, NUM_DETECTIONS,4]
    // contains the location of detected boxes
    private var outputLocations: Array<Array<FloatArray>> = arrayOf()

    // outputClasses: array of shape [Batchsize, NUM_DETECTIONS]
    // contains the classes of detected boxes
    private var outputClasses: Array<FloatArray> = arrayOf()

    // outputScores: array of shape [Batchsize, NUM_DETECTIONS]
    // contains the scores of detected boxes
    private var outputScores: Array<FloatArray> = arrayOf()

    // numDetections: array of shape [Batchsize]
    // contains the number of detected boxes
    private var numDetections: FloatArray = floatArrayOf()
    private var embeedings: Array<FloatArray> = arrayOf()
    private var imgData: ByteBuffer? = null
    private var tfLite: Interpreter? = null

    private val registered = HashMap<String, Recognition>()

    override fun register(name: String, recognition: Recognition) {
        registered[name] = recognition
    }

    // looks for the nearest embeeding in the dataset (using L2 norm)
    // and retrurns the pair <id, distance>
    private fun findNearest(emb: FloatArray): Triple<String, Float,Float>? {
        var ret: Triple<String, Float,Float>? = null
        for ((key, value) in registered) {
            val knownEmb = value.extra[0]

            var distance = 0f
            var dotProduct = 0.0f
            var norm1 = 0.0f
            var norm2 = 0.0f

            for (i in emb.indices) {
                //cosine similarity
                dotProduct += emb[i] * knownEmb[i]
                norm1 += emb[i] * emb[i]
                norm2 += knownEmb[i] * knownEmb[i]
                //L2 - Euclidean
                /*val diff = emb[i] - knownEmb[i]
                distance += diff * diff*/
                //L1 - Manhattan
                distance += abs(emb[i] - knownEmb[i])
            }
            /*distance = sqrt(distance)*/
            val cosineSimilarity = dotProduct / (sqrt(norm1.toDouble()) * sqrt(norm2.toDouble()))
            Log.d("TAG", "findNearestdistance: $distance : $key : $cosineSimilarity")
            if (ret == null || (distance < ret.second && cosineSimilarity > 0.7f)) {
                ret = Triple(key, distance,cosineSimilarity.toFloat())
            }
        }
        return ret
    }

    private fun findCosineSimilarity(embedding : FloatArray) : Pair<String, Float>? {
        var ret: Pair<String, Float>? = null
        for ((key,value) in registered) {
            val knownEmbedding = value.extra[0]

            var dotProduct = 0.0f
            var norm1 = 0.0f
            var norm2 = 0.0f

            for (i in embedding.indices) {
                dotProduct += embedding[i] * knownEmbedding[i]
                norm1 += embedding[i] * embedding[i]
                norm2 += knownEmbedding[i] * knownEmbedding[i]
            }
            val cosineSimilarity = dotProduct / (sqrt(norm1.toDouble()) * sqrt(norm2.toDouble()))
            Log.d("TAG", "findCosineSimilarity: $cosineSimilarity : $key")

            if (ret == null || cosineSimilarity > ret.second) {
                ret = Pair(key, cosineSimilarity.toFloat())
            }
        }
        return ret
    }


    override fun recognizeImage(bitmap: Bitmap, getExtra: Boolean): List<Recognition> {
        // Preprocess the image data from 0-255 int to normalized float based
        // on the provided parameters.
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        imgData?.rewind()
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val pixelValue = intValues[i * inputSize + j]
                if (isModelQuantized) {
                    // Quantized model
                    imgData?.put((pixelValue shr 16 and 0xFF).toByte())
                    imgData?.put((pixelValue shr 8 and 0xFF).toByte())
                    imgData?.put((pixelValue and 0xFF).toByte())
                } else { // Float model
                    imgData?.putFloat(((pixelValue shr 16 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                    imgData?.putFloat(((pixelValue shr 8 and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                    imgData?.putFloat(((pixelValue and 0xFF) - IMAGE_MEAN) / IMAGE_STD)
                }
            }
        }

        // Copy the input data into TensorFlow.
        val inputArray = arrayOf<Any?>(imgData)
        val outputMap: MutableMap<Int, Any> = HashMap()
        embeedings = Array(1) {
            FloatArray(
                OUTPUT_SIZE
            )
        }
        outputMap[0] = embeedings
        tfLite?.runForMultipleInputsOutputs(inputArray, outputMap)

        var distance = Float.MAX_VALUE
        var cosineValue = Float.MAX_VALUE
        val id = "0"
        var label: String? = "?"
        if (registered.size > 0) {
            val nearest = findNearest(embeedings[0])
            if (nearest != null) {
                label = nearest.first
                distance = nearest.second
                cosineValue = nearest.third
            }
        }
        val numDetectionsOutput = 1
        val recognitions = ArrayList<Recognition>(numDetectionsOutput)
        val rec = Recognition(
            id = id,
            title = label,
            distance = distance,
            cosineValue = cosineValue,
            extra = if (getExtra) embeedings else arrayOf()
        )
        recognitions.add(rec)
        return recognitions
    }

    override fun clearRegisteredFace() {
        registered.clear()
    }

    companion object {

        //private static final int OUTPUT_SIZE = 512;
        private const val OUTPUT_SIZE = 192

        // Only return this many results.
        private const val NUM_DETECTIONS = 1

        // Float model
        private const val IMAGE_MEAN = 128.0f
        private const val IMAGE_STD = 128.0f

        // Number of threads in the java app
        private const val NUM_THREADS = 2

        /** Memory-map the model file in Assets.  */
        @Throws(IOException::class)
        private fun loadModelFile(assets: AssetManager, modelFilename: String): MappedByteBuffer {
            val fileDescriptor = assets.openFd(modelFilename)
            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
            val fileChannel = inputStream.channel
            val startOffset = fileDescriptor.startOffset
            val declaredLength = fileDescriptor.declaredLength
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        }

        /**
         * Initializes a native TensorFlow session for classifying images.
         *
         * @param assetManager The asset manager to be used to load assets.
         * @param modelFilename The filepath of the model GraphDef protocol buffer.
         * @param labelFilename The filepath of label file for classes.
         * @param inputSize The size of image input
         * @param isQuantized Boolean representing model is quantized or not
         */
        @Throws(IOException::class)
        fun create(
            assetManager: AssetManager,
            modelFilename: String,
            labelFilename: String,
            inputSize: Int,
            isQuantized: Boolean = false,
        ): SimilarityClassifier {
            val d = TFLiteObjectDetectionAPIModel()
            val actualFilename = labelFilename.split("file:///android_asset/".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()[1]
            val labelsInput = assetManager.open(actualFilename)
            val br = BufferedReader(InputStreamReader(labelsInput))
            var line: String?
            while (br.readLine().also { line = it } != null) {
                d.labels.add(line)
            }
            br.close()
            d.inputSize = inputSize
            val options = Interpreter.Options()
            options.numThreads = NUM_THREADS
            try {
                d.tfLite = Interpreter(loadModelFile(assetManager, modelFilename), options)
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
            d.isModelQuantized = isQuantized
            // Pre-allocate buffers.
            val numBytesPerChannel: Int = if (isQuantized) {
                1 // Quantized
            } else {
                4 // Floating point
            }
            d.imgData = ByteBuffer.allocateDirect(1 * d.inputSize * d.inputSize * 3 * numBytesPerChannel)
            d.imgData?.order(ByteOrder.nativeOrder())
            d.intValues = IntArray(d.inputSize * d.inputSize)
            d.outputLocations = Array(1) {
                Array(NUM_DETECTIONS) {
                    FloatArray(
                        4
                    )
                }
            }
            d.outputClasses = Array(1) {
                FloatArray(
                    NUM_DETECTIONS
                )
            }
            d.outputScores = Array(1) {
                FloatArray(
                    NUM_DETECTIONS
                )
            }
            d.numDetections = FloatArray(1)
            return d
        }
    }
}