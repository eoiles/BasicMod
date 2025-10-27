package GameMaster.cards;

import GameMaster.character.MyCharacter;
import GameMaster.powers.SplashPower;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Splash extends BaseCard {
    public static final String ID = makeID(Splash.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,      // tune rarity as desired
            CardTarget.SELF,
            1                     // cost
    );

    private static final int PERCENT = 25; // mirrors 25% of dealt damage

    public Splash() {
        super(ID, info);
        setMagic(PERCENT, 25); // after upgrade, mirrors 50% of dealt damage
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new SplashPower(p, magicNumber), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() { return new Splash(); }
}
