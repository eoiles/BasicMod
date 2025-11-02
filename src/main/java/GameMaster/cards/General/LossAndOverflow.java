package GameMaster.cards.General;

import GameMaster.cards.BaseCard;
import GameMaster.character.MyCharacter;
import GameMaster.powers.LossAndOverflowPower;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class LossAndOverflow extends BaseCard {
    public static final String ID = BaseCard.packedID(LossAndOverflow.class);
    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR, CardType.POWER, CardRarity.RARE, CardTarget.SELF, 2
    );
    private static final int CAP = 6;      // base
    private static final int UPG_DELTA = 2; // upgrade => 10

    public LossAndOverflow() {
        super(ID, info);
        setMagic(CAP, UPG_DELTA); // !M! == cap
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new LossAndOverflowPower(p, magicNumber), magicNumber));
    }
}
