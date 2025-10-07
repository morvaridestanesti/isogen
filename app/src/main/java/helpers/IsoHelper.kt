package helpers

import android.content.Context
import com.estanesti.isogen.App
import com.solab.iso8583.IsoMessage
import com.solab.iso8583.IsoType
import com.solab.iso8583.MessageFactory
import com.solab.iso8583.parse.ConfigParser
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object IsoHelper {
    fun buildMessage(cardNumber: String, amount: String): String {
        return try {
            val messageFactory: MessageFactory<IsoMessage> = ConfigParser.createDefault()
            val isoMessage: IsoMessage = messageFactory.newMessage(0x200)
            isoMessage.setValue(2, cardNumber, IsoType.LLVAR, 19)
            isoMessage.setValue(4, amount, IsoType.AMOUNT, 12)
            isoMessage.setValue(7, getDate(), IsoType.DATE10, 12)
            isoMessage.setValue(11, getStan(), IsoType.NUMERIC, 6)

            String(isoMessage.writeData()) // return final ISO8583 message string
        } catch (e: Exception) {
            "ERROR: ${e.message}"
        }
    }

    fun parseMessage(message: String, context: Context): String {
        return try {
            val inputStream: InputStream = context.assets.open("fields.xml")
            val reader = InputStreamReader(inputStream)
            val messageFactory: MessageFactory<IsoMessage> = MessageFactory()
            ConfigParser.configureFromReader(messageFactory, reader)

            val isoMessage: IsoMessage = messageFactory.parseMessage(message.toByteArray(), 0)

            buildString {
                append("Card Number: ${isoMessage.getField<Any>(2)}\n")
                append("Amount: ${isoMessage.getField<Any>(4)}\n")
                append("DateTime: ${isoMessage.getField<Any>(7)}\n")
                append("STAN: ${isoMessage.getField<Any>(11)}")
            }
        } catch (e: Exception) {
            "ERROR: ${e.message}"
        }
    }

    private fun getStan(): String {
        var stan = App.PREFERENCES.getInt("STAN", 0) + 1
        if (stan > 999999) stan = 1

        App.EDITOR.putInt("STAN", stan).apply()
        return String.format(Locale.US, "%06d", stan)
    }

    private fun getDate(): String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmss"))
}
