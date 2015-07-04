package eu.timepit.refined

import eu.timepit.refined.InferenceRuleAlias.==>
import eu.timepit.refined.string._
import shapeless.Witness

import scala.util.Try

object string extends StringPredicates with StringInferenceRules {

  /** Predicate that checks if a `String` ends with the suffix `S`. */
  trait EndsWith[S]

  /** Predicate that checks if a `String` matches the regular expression `R`. */
  trait MatchesRegex[R]

  /** Predicate that checks if a `String` is a valid regular expression. */
  trait Regex

  /** Predicate that checks if a `String` starts with the prefix `S`. */
  trait StartsWith[S]

  /** Predicate that checks if a `String` is a valid URI. */
  trait Uri

  /** Predicate that checks if a `String` is a valid URL. */
  trait Url
}

trait StringPredicates {

  implicit def endsWithPredicate[R <: String](implicit wr: Witness.Aux[R]): Predicate[EndsWith[R], String] =
    Predicate.instance(_.endsWith(wr.value), t => s""""$t".endsWith("${wr.value}")""")

  implicit def matchesRegexPredicate[R <: String](implicit wr: Witness.Aux[R]): Predicate[MatchesRegex[R], String] =
    Predicate.instance(_.matches(wr.value), t => s""""$t".matches("${wr.value}")""")

  implicit val regexPredicate: Predicate[Regex, String] =
    Predicate.fromTry(t => Try(t.r), t => s"""isValidRegex("$t")""")

  implicit def startsWithPredicate[R <: String](implicit wr: Witness.Aux[R]): Predicate[StartsWith[R], String] =
    Predicate.instance(_.startsWith(wr.value), t => s""""$t".startsWith("${wr.value}")""")

  implicit val uriPredicate: Predicate[Uri, String] =
    Predicate.fromTry(t => Try(new java.net.URI(t)), t => s"""isValidUri("$t")""")

  implicit val urlPredicate: Predicate[Url, String] =
    Predicate.fromTry(t => Try(new java.net.URL(t)), t => s"""isValidUrl("$t")""")
}

trait StringInferenceRules {

  implicit def endsWithInference[A <: String, B <: String](implicit wa: Witness.Aux[A], wb: Witness.Aux[B]): EndsWith[A] ==> EndsWith[B] =
    InferenceRule(wa.value.endsWith(wb.value), s"endsWithInference(${wa.value}, ${wb.value})")

  implicit def startsWithInference[A <: String, B <: String](implicit wa: Witness.Aux[A], wb: Witness.Aux[B]): StartsWith[A] ==> StartsWith[B] =
    InferenceRule(wa.value.startsWith(wb.value), s"startsWithInference(${wa.value}, ${wb.value})")
}