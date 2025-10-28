package GameMaster.cards;

import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.*;

public class DronePack extends BaseCard {
    public static final String ID = makeID(DronePack.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.RARE,
            CardTarget.SELF,
            2 // cost (tune if you like)
    );

    private static final int DRONES = 3;
    private static final int UPG_DELTA = 3; // 3 -> 6

    public DronePack() {
        super(ID, info);
        setMagic(DRONES, UPG_DELTA); // !M! = number of random orbs
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot(new ChannelAction(randomOrb()));
        }
    }

    private static AbstractOrb randomOrb() {
        int roll = AbstractDungeon.cardRandomRng.random(0, 3); // Lightning/Frost/Dark/Plasma
        switch (roll) {
            case 0: return new Lightning();
            case 1: return new Frost();
            case 2: return new Dark();
            default: return new Plasma();
        }
    }

    @Override
    public AbstractCard makeCopy() { return new DronePack(); }
}
