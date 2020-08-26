package com.passwordgenerator

import me.gosimple.nbvcxz.Nbvcxz
import me.gosimple.nbvcxz.resources.ConfigurationBuilder
import me.gosimple.nbvcxz.scoring.TimeEstimate
import java.util.*

fun getPasswordStrengthMessage(password: String): String {

    val configuration = ConfigurationBuilder()
        .setLocale(Locale.forLanguageTag("en"))
        .createConfiguration()

    val nbvcxz = Nbvcxz(configuration)
    var result = nbvcxz.estimate(password)

    val entropy:String  = getStringFromDouble(result.entropy)

    val timeToCrackOff =
        TimeEstimate.getTimeToCrackFormatted(result, "OFFLINE_BCRYPT_12")
    val timeToCrackOn =
        TimeEstimate.getTimeToCrackFormatted(result, "ONLINE_THROTTLED")

    if (result.isMinimumEntropyMet) {
        val successMessage = java.lang.StringBuilder()
        successMessage.append("Password has met the minimum strength requirements.\n")
        successMessage.append("Entropy: ").append(entropy).append("\n")
        successMessage.append("Time to crack - online: ").append(timeToCrackOn).append("\n")
        successMessage.append("Time to crack - offline: ").append(timeToCrackOff).append("\n")

        return successMessage.toString()

    } else {

        val feedback = result.feedback

        val errorMessage = java.lang.StringBuilder()
        errorMessage.append("Password does not meet the minimum strength requirements.\n")
        errorMessage.append("Entropy: ").append(entropy).append("\n")
        errorMessage.append("Time to crack - online: ").append(timeToCrackOn)
            .append("\n")
        errorMessage.append("sdafTime to crack - offline: ").append(timeToCrackOff)
            .append("\n")
        if (feedback != null) {
            if (feedback.warning != null) errorMessage.append("Warning: ")
                .append(feedback.warning).append("\n")
            for (suggestion in feedback.suggestion) {
                errorMessage.append("Suggestion: ").append(suggestion).append("\n")
            }
        }
        return errorMessage.toString()
    }
}

fun getStringFromDouble(value : Double) : String
{
    return String.format("%.1f", value)
}
