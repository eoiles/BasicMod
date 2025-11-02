package GameMaster;

import GameMaster.cards.BaseCard;
import basemod.BaseMod;
import basemod.AutoAdd;                               // ← already added
import basemod.interfaces.AddAudioSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.EditCharactersSubscriber;   // ← already added
import basemod.interfaces.EditCardsSubscriber;        // ← already added
import basemod.interfaces.EditRelicsSubscriber;       // ← NEW

import GameMaster.character.MyCharacter;              // ← already added
import GameMaster.relics.BaseRelic;                   // ← NEW

import GameMaster.util.GeneralUtils;
import GameMaster.util.KeywordInfo;
import GameMaster.util.Sounds;
import GameMaster.util.TextureLoader;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SpireInitializer
public class BasicMod implements
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        AddAudioSubscriber,
        PostInitializeSubscriber,
        EditCharactersSubscriber,                     // ← keep
        EditCardsSubscriber,                          // ← keep
        EditRelicsSubscriber {                        // ← NEW
    public static ModInfo info;
    public static String modID; //Edit your pom.xml to change this
    static { loadModInfo(); }
    private static final String resourcesFolder = checkResourcesPath();
    public static final Logger logger = LogManager.getLogger(modID); //Used to output to the console.

    //Prefix helper
    public static String makeID(String id) {
        return modID + ":" + id;
    }

    //Called by ModTheSpire
    public static void initialize() {
        new BasicMod();
        MyCharacter.Meta.registerColor();              // keep
    }

    public BasicMod() {
        BaseMod.subscribe(this);
        logger.info(modID + " subscribed to BaseMod.");
    }

    @Override
    public void receivePostInitialize() {
        Texture badgeTexture = TextureLoader.getTexture(imagePath("badge.png"));
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, null);
    }

    /* ---------- Character registration ---------- */
    @Override
    public void receiveEditCharacters() {
        MyCharacter.Meta.registerCharacter();
    }

    /* ---------- Cards: AutoAdd setup ---------- */
    @Override
    public void receiveEditCards() {
        new AutoAdd(modID)
                .packageFilter(BaseCard.class)
                .setDefaultSeen(true)
                .cards();
    }

    /* ---------- Relics: AutoAdd setup ---------- */
    @Override
    public void receiveEditRelics() {
        new AutoAdd(modID)
                .packageFilter(BaseRelic.class)
                .any(BaseRelic.class, (info, relic) -> {
                    if (relic.pool != null) {
                        BaseMod.addRelicToCustomPool(relic, relic.pool);
                    } else {
                        BaseMod.addRelic(relic, relic.relicType);
                    }
                    if (info.seen) {
                        com.megacrit.cardcrawl.unlock.UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
                });
    }

    /*---------- Localization ----------*/
    private static String getLangString() { return Settings.language.name().toLowerCase(); }
    private static final String defaultLanguage = "eng";

    public static final Map<String, KeywordInfo> keywords = new HashMap<>();

    @Override
    public void receiveEditStrings() {
        loadLocalization(defaultLanguage);
        if (!defaultLanguage.equals(getLangString())) {
            try { loadLocalization(getLangString()); }
            catch (GdxRuntimeException e) { e.printStackTrace(); }
        }
    }

    private void loadLocalization(String lang) {
        BaseMod.loadCustomStringsFile(CardStrings.class,  localizationPath(lang, "CardStrings.json"));
        BaseMod.loadCustomStringsFile(CharacterStrings.class, localizationPath(lang, "CharacterStrings.json"));
        BaseMod.loadCustomStringsFile(EventStrings.class,   localizationPath(lang, "EventStrings.json"));
        BaseMod.loadCustomStringsFile(OrbStrings.class,     localizationPath(lang, "OrbStrings.json"));
        BaseMod.loadCustomStringsFile(PotionStrings.class,  localizationPath(lang, "PotionStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class,   localizationPath(lang, "PowerStrings.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class,   localizationPath(lang, "RelicStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class,      localizationPath(lang, "UIStrings.json"));
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(localizationPath(defaultLanguage, "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        KeywordInfo[] kws = gson.fromJson(json, KeywordInfo[].class);
        for (KeywordInfo k : kws) { k.prep(); registerKeyword(k); }

        if (!defaultLanguage.equals(getLangString())) {
            try {
                json = Gdx.files.internal(localizationPath(getLangString(), "Keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
                kws = gson.fromJson(json, KeywordInfo[].class);
                for (KeywordInfo k : kws) { k.prep(); registerKeyword(k); }
            } catch (Exception e) {
                logger.warn(modID + " does not support " + getLangString() + " keywords.");
            }
        }
    }

    private void registerKeyword(KeywordInfo info) {
        BaseMod.addKeyword(modID.toLowerCase(), info.PROPER_NAME, info.NAMES, info.DESCRIPTION, info.COLOR);
        if (!info.ID.isEmpty()) { keywords.put(info.ID, info); }
    }

    @Override
    public void receiveAddAudio() {
        loadAudio(Sounds.class);
    }

    private static final String[] AUDIO_EXTENSIONS = { ".ogg", ".wav", ".mp3" };
    private void loadAudio(Class<?> cls) {
        try {
            Field[] fields = cls.getDeclaredFields();
            outer:
            for (Field f : fields) {
                int modifiers = f.getModifiers();
                if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers) && f.getType().equals(String.class)) {
                    String s = (String) f.get(null);
                    if (s == null) {
                        s = audioPath(f.getName());
                        for (String ext : AUDIO_EXTENSIONS) {
                            String testPath = s + ext;
                            if (Gdx.files.internal(testPath).exists()) {
                                s = testPath;
                                BaseMod.addAudio(s, s);
                                f.set(null, s);
                                continue outer;
                            }
                        }
                        throw new Exception("Failed to find audio file \"" + f.getName() + "\" in " + resourcesFolder + "/audio.");
                    } else {
                        if (Gdx.files.internal(s).exists()) {
                            BaseMod.addAudio(s, s);
                        } else {
                            throw new Exception("Failed to find audio file \"" + s + "\"; check the filepath.");
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception occurred in loadAudio: ", e);
        }
    }

    // path helpers
    public static String localizationPath(String lang, String file) { return resourcesFolder + "/localization/" + lang + "/" + file; }
    public static String audioPath(String file) { return resourcesFolder + "/audio/" + file; }
    public static String imagePath(String file) { return resourcesFolder + "/images/" + file; }
    public static String characterPath(String file) { return resourcesFolder + "/images/character/" + file; }
    public static String powerPath(String file) { return resourcesFolder + "/images/powers/" + file; }
    public static String relicPath(String file) { return resourcesFolder + "/images/relics/" + file; }

    private static String checkResourcesPath() {
        String name = BasicMod.class.getName();
        int separator = name.indexOf('.');
        if (separator > 0) name = name.substring(0, separator);

        FileHandle resources = new LwjglFileHandle(name, Files.FileType.Internal);

        if (!resources.exists()) {
            throw new RuntimeException("\n\tFailed to find resources folder; expected it to be at  \"resources/" + name + "\".");
        }
        if (!resources.child("images").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'images' folder in the mod's 'resources/" + name + "' folder.");
        }
        if (!resources.child("localization").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'localization' folder in the mod's 'resources/" + name + "' folder.");
        }
        return name;
    }

    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo)->{
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null) return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(BasicMod.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        } else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }
}
