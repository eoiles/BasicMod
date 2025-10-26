package GameMaster.cards;

import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static GameMaster.BasicMod.makeID;

public class Defend extends BaseCard {
    public static final String ID = makeID(Defend.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.BASIC,
            CardTarget.SELF,
            1   // cost
    );

    private static final int BLOCK = 5;
    private static final int UPG_BLOCK = 0; // no upgrade change

    public Defend() {
        super(ID, info);
        setBlock(BLOCK, UPG_BLOCK);
        tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public AbstractCard makeCopy() { return new Defend(); }
}
