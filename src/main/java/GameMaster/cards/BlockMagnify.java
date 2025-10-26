package GameMaster.cards;

import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BlockMagnify extends BaseCard {
    public static final String ID = makeID(BlockMagnify.class.getSimpleName());

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1 // cost
    );

    private static final int MULTIPLIER = 10;

    public BlockMagnify() {
        super(ID, info);
        setMagic(MULTIPLIER, 0);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SelectCardsInHandAction(
                1,
                "Choose a card to magnify its Block.",
                cards -> {
                    for (AbstractCard c : cards) {
                        c.baseBlock *= magicNumber;
                        c.isBlockModified = true;
                        c.applyPowers();
                        c.flash();
                    }
                }
        ));
    }

    @Override
    public AbstractCard makeCopy() { return new BlockMagnify(); }
}
