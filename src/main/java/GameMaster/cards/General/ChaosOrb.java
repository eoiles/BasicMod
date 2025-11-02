package GameMaster.cards.General;

import GameMaster.cards.BaseCard;
import GameMaster.character.MyCharacter;
import GameMaster.powers.ChaosOrbPower;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ChaosOrb extends BaseCard {
    public static final String ID = BaseCard.packedID(ChaosOrb.class);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.POWER,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1 // cost
    );

    private static final int DEBUFFS_PER_ATTACK = 1;
    private static final int UPGRADE_DELT = 1; // 1 -> 2

    public ChaosOrb() {
        super(ID, info);
        setMagic(DEBUFFS_PER_ATTACK, UPGRADE_DELT);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ChaosOrbPower(p, magicNumber), magicNumber));
    }

    @Override
    public AbstractCard makeCopy() { return new ChaosOrb(); }
}
