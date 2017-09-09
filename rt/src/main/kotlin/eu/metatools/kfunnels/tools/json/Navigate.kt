package eu.metatools.kfunnels.tools.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonToken

/**
 * Basic JSON navigation.
 */
sealed class Navigation {
    companion object {
        /**
         * Parses the string as a navigation, where consecutive steps are denoted by a '/', pure numeric entries
         * are mapped as a navigation into the n-th element ([IntoElement]), otherwise the entries are mapped as
         * navigation into a field ([IntoField]).
         */
        fun parse(string: String) =
                string.split('/').map {
                    if (it.all { it.isDigit() })
                        IntoElement(it.toInt())
                    else
                        IntoField(it)
                }.reduce { a, b -> Compose(a, b) }
    }
}

/**
 * Navigates into the field [label].
 */
class IntoField(val label: String) : Navigation()

/**
 * Navigates into the n-th element of an array.
 */
class IntoElement(val nth: Int) : Navigation()

/**
 * Composes the navigation sequentially.
 */
class Compose(val first: Navigation, val second: Navigation) : Navigation()

/**
 * Advances the parser for the given [Navigation].
 */
fun JsonParser.navigate(navigation: Navigation) {
    when (navigation) {
        is IntoField -> {
            check(nextToken() == JsonToken.START_OBJECT) { "Not at an object, at $currentToken." }

            while (true) {
                // Get next token in parser
                val token = nextToken()

                // If structure ended, the label was not found
                if (token == JsonToken.END_OBJECT)
                    error("Cannot find label ${navigation.label}.")

                // Otherwise, there needs to be a field now
                check(token == JsonToken.FIELD_NAME)

                // If current name is the requested label, this navigation step is over
                if (currentName == navigation.label)
                    return

                // Otherwise, skip children if not a value node
                if (nextToken().isStructStart)
                    skipChildren()
            }
        }

        is IntoElement -> {
            check(nextToken() == JsonToken.START_ARRAY) { "Not at an array, at $currentToken." }

            for (i in 1..navigation.nth) {
                // Get next token in parser
                val token = nextToken()

                // If structure ended, the label was not found
                if (token == JsonToken.END_ARRAY)
                    error("Cannot find ${navigation.nth}-th element in array.")

                // Otherwise, skip children if not a value node
                if (token.isStructStart)
                    skipChildren()
            }
        }
        is Compose -> {
            // Do first, then second navigation
            navigate(navigation.first)
            navigate(navigation.second)
        }
    }
}

/**
 * Wrapper for [navigate], returns the original parser.
 */
operator fun JsonParser.get(navigation: Navigation) = apply {
    navigate(navigation)
}

/**
 * Wrapper for [navigate] where the string is parsed by [Navigation.parse], returns the original parser.
 */
operator fun JsonParser.get(string: String) = apply {
    navigate(Navigation.parse(string))
}