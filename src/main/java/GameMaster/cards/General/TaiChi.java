package GameMaster.cards.General;

import GameMaster.cards.BaseCard;
import GameMaster.character.MyCharacter;
import GameMaster.powers.TaiChiPower;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TaiChi extends BaseCard {
    public static final String ID = BaseCard.packedID(TaiChi.class);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1 // cost
    );

    private static final int AMOUNT = 1; // block gained / damage dealt per trigger

    public TaiChi() {
        super(ID, info);
        setMagic(AMOUNT, AMOUNT); // upgrade would add +1
        initializeDescription(); // <-- parse #y, !M!, NL, keywords, etc.
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new TaiChiPower(p, magicNumber)));
    }

    @Override
    public AbstractCard makeCopy() { return new TaiChi(); }
}
