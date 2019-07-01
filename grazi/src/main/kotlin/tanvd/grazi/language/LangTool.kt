package tanvd.grazi.language

import org.languagetool.*
import tanvd.grazi.GraziConfig
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

object LangTool {
    private val langs: MutableMap<Lang, JLanguageTool> = ConcurrentHashMap()

    private const val cacheMaxSize = 25_000L
    private const val cacheExpireAfterMinutes = 5

    operator fun get(lang: Lang): JLanguageTool {
        return langs.getOrPut(lang) {
            val cache = ResultCache(cacheMaxSize, cacheExpireAfterMinutes, TimeUnit.MINUTES)
            JLanguageTool(lang.jLanguage, GraziConfig.state.nativeLanguage.jLanguage,
                    cache, UserConfig(GraziConfig.state.userWords.toList())).apply {
                lang.configure(this)
                disableRules(allActiveRules.map { it.id }.filter { it in GraziConfig.state.userDisabledRules })
                //Spellcheck will be done by Grazi spellchecker
                disableRules(allActiveRules.filter { it.isDictionaryBasedSpellingRule }.map { it.id })
            }
        }
    }

    fun reset() {
        langs.clear()
    }

    fun init(enabledLangs: Collection<Lang>) {
        for (lang in enabledLangs) {
            get(lang)
        }
    }
}