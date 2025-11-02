package GameMaster.cards.General;

import GameMaster.cards.BaseCard;
import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Plasma;

public class DronePack extends BaseCard {
    public static final String ID = BaseCard.packedID(DronePack.class);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            2 // cost (adjust if needed)
    );

    private static final int COUNT = 3;     // slots & orbs
    private static final int UPG_DELTA = 3; // 3 -> 6 on upgrade

    public DronePack() {
        super(ID, info);
        setMagic(COUNT, UPG_DELTA); // !M! = both slots gained and orbs channeled
        // setExhaust(true); // uncomment if you want it to Exhaust
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // Gain slots first, then channel
        addToBot(new IncreaseMaxOrbAction(magicNumber));
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new ChannelAction(randomOrb()));
        }
    }

    private static AbstractOrb randomOrb() {
        int roll = AbstractDungeon.cardRandomRng.random(0, 2); // Lightning/Frost/Dark/Plasma
        switch (roll) {
            case 0: return new Lightning();
            case 1: return new Frost();
            //case 2: return new Dark();
            default: return new Plasma();
        }
    }

    @Override
    public AbstractCard makeCopy() { return new DronePack(); }
}
