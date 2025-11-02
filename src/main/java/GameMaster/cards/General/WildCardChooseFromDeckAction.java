package GameMaster.cards.General;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class WildCardChooseFromDeckAction extends AbstractGameAction {
    private boolean opened = false;
    private CardGroup choices;
    private final boolean upgradeChosen;

    public WildCardChooseFromDeckAction(boolean upgradeChosen) {
        this.upgradeChosen = upgradeChosen;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;

    }

    @Override
    public void update() {
        if (!opened) {
            choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            // Source = MASTER DECK (whole deck), not draw pile
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.type == AbstractCard.CardType.STATUS || c.type == AbstractCard.CardType.CURSE) continue;
                choices.addToBottom(c.makeStatEquivalentCopy());
            }
            if (choices.isEmpty()) { isDone = true; return; }

            opened = true;
            AbstractDungeon.gridSelectScreen.open(
                    choices, 1,
                    "Choose a card from your deck. Add it to your hand.",
                    false, false, false, false
            );
            tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            AbstractCard picked = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            AbstractCard toHand = picked.makeStatEquivalentCopy();
            if (upgradeChosen && toHand.canUpgrade()) toHand.upgrade();

            // Hand full? Put on top of draw pile instead of discarding.
            if (AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) {
                addToTop(new MakeTempCardInDrawPileAction(toHand, 1, true, true, false));
            } else {
                addToTop(new MakeTempCardInHandAction(toHand, 1));
            }
            isDone = true;
            return;
        }

        tickDuration();
    }
}
