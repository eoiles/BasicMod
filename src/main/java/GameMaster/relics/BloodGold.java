package GameMaster.relics;

import basemod.AutoAdd; // for @AutoAdd.Seen
import GameMaster.character.MyCharacter;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static GameMaster.BasicMod.makeID;

@AutoAdd.Seen
public class BloodGold extends BaseRelic {
    private static final String NAME = "BloodGold";
    public static final String ID = makeID(NAME);
    private static final RelicTier RARITY = RelicTier.RARE;
    private static final LandingSound SFX = LandingSound.CLINK;

    public BloodGold() {
        // character-specific relic constructor
        super(ID, NAME, MyCharacter.Meta.CARD_COLOR, RARITY, SFX);
    }

    /** React when HP is lost (we don't modify the loss). */
    @Override
    public void onLoseHp(int damageAmount) {
        if (damageAmount > 0) {
            flash();
            AbstractDungeon.actionManager.addToBottom(new GainGoldAction(damageAmount));
        }
    }

    @Override
    public String getUpdatedDescription() {
        // Add text in RelicStrings.json; this returns that text.
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BloodGold();
    }
}
