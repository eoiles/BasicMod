package GameMaster.cards.General;

import GameMaster.cards.BaseCard;
import GameMaster.character.MyCharacter;
import GameMaster.util.CardStats;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BlockMagnify extends BaseCard {
    public static final String ID = BaseCard.packedID(BlockMagnify.class);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.UNCOMMON,
            CardTarget.SELF,
            1 // cost
    );

    private static final int MULTIPLIER = 2;

    public BlockMagnify() {
        super(ID, info);
        setMagic(MULTIPLIER, 1);
        setExhaust(true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SelectCardsInHandAction(
                1,
                "Choose a Defend to magnify its Block.",
                // only allow the basic Defend (uses the STARTER_DEFEND tag)
                c -> c.hasTag(AbstractCard.CardTags.STARTER_DEFEND),
                cards -> {
                    if (cards.isEmpty()) return;
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
