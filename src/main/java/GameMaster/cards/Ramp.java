package GameMaster.cards;

import GameMaster.character.MyCharacter;
import GameMaster.powers.RampPower;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Ramp extends BaseCard {
    public static final String ID = makeID(Ramp.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.RARE,
            CardTarget.SELF,
            3 // cost
    );

    private static final int ENERGY_UP = 1;

    public Ramp() {
        super(ID, info);
        setMagic(ENERGY_UP, 0);   // !M! = +1 max energy
        setExhaust(true);
        initializeDescription(); // <-- parse #y, !M!, NL, keywords, etc.
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new RampPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(2); // 3 -> 2
        }
    }

    @Override
    public AbstractCard makeCopy() { return new Ramp(); }
}
