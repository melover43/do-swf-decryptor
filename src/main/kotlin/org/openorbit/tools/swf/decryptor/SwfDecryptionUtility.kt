package org.openorbit.tools.swf.decryptor

import org.openorbit.tools.swf.decryptor.MainEncryptionAlgorithmFinder.determineEncryptionAlgorithm
import org.openorbit.tools.swf.decryptor.MainEncryptionKeyParser.parseEncryptionKey
import org.openorbit.tools.swf.decryptor.decryptors.LoadingScreenDecryptionHandler.decryptLoadingScreen
import java.io.File
import java.io.FileNotFoundException

class SwfDecryptionUtility(private val workingDirectory: String) {

    // the preloader knows the decryption algorithm
    private val filePreloader: File = File("$workingDirectory/preloader.swf")
    private val fileLoadingScreen: File = File("$workingDirectory/loadingScreen.swf")
    private val fileMain: File = File("$workingDirectory/main.swf")

    init {
        // assert that all files exist before starting to work on them. if one is missing, decryption will fail
        arrayOf(fileMain, filePreloader, fileLoadingScreen).iterator().forEach {
            if (!it.exists()) {
                throw FileNotFoundException("File ${it.name} at path ${it.path} not found!")
            }
        }
    }

    private fun decryptLoadingScreen(): File {
        val decryptedFile = decryptLoadingScreen(fileLoadingScreen.readBytes())
        return File("$workingDirectory/loadingscreen_decrypted.swf").apply { writeBytes(decryptedFile) }
    }


    fun decrypt() {
        println("The algorithm is " + determineEncryptionAlgorithm(filePreloader))
        val file = decryptLoadingScreen()
        println("loadingscreen decrypted")
        println("EncryptionKey: " + parseEncryptionKey(file))
    }
}