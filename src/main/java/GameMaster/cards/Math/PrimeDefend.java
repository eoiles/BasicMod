package GameMaster.cards.Math;

import GameMaster.cards.BaseCard;
import GameMaster.character.MyCharacter;
import GameMaster.powers.PrimeStackPower;
import GameMaster.util.CardStats;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PrimeDefend extends BaseCard {
    public static final String ID = BaseCard.packedID(PrimeDefend.class);

    private static final CardStats info = new CardStats(
            MyCharacter.Meta.CARD_COLOR,
            CardType.SKILL,
            CardRarity.COMMON,
            CardTarget.SELF,
            1 // upgrades to 0
    );

    private static final CardStrings STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public PrimeDefend() {
        super(ID, info);
        setBlock(0, 0);
    }

    // --- live preview: show NEXT prime as block ---
    @Override
    public void applyPowers() {
        int cur = getCurrentPrimeStack();
        int next = nextPrime(cur);
        this.baseBlock = next;
        super.applyPowers();
        updateDynamicDescription(next);
    }

    @Override
    public void onMoveToDiscard() { resetBaseDescription(); }

    private void updateDynamicDescription(int next) {
        String base = (STRINGS != null && STRINGS.DESCRIPTION != null) ? (upgraded ? STRINGS.UPGRADE_DESCRIPTION : STRINGS.DESCRIPTION) : "Gain next prime Block.";
        if (base.contains("next prime"))
            this.rawDescription = base.replace("next prime", "next prime (" + next + ")");
        else
            this.rawDescription = base + " (" + next + ")";
        initializeDescription();
    }

    private void resetBaseDescription() {
        if (STRINGS != null) {
            this.rawDescription = upgraded && STRINGS.UPGRADE_DESCRIPTION != null ? STRINGS.UPGRADE_DESCRIPTION : STRINGS.DESCRIPTION;
        }
        initializeDescription();
    }

    // --- on use: apply stack first, then gain using the NEW value ---
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int cur = p.hasPower(PrimeStackPower.POWER_ID) ? p.getPower(PrimeStackPower.POWER_ID).amount : 0;
        int next = nextPrime(cur);
        int delta = next - cur;

        addToBot(new ApplyPowerAction(p, p, new PrimeStackPower(p, delta), delta));

        int prevBase = this.baseBlock;
        this.baseBlock = next;
        super.applyPowers(); // Dex etc.
        int finalBlock = this.block;
        this.baseBlock = prevBase;

        addToBot(new GainBlockAction(p, p, finalBlock));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(0);
            resetBaseDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() { return new PrimeDefend(); }

    // ---- helpers ----
    private static int getCurrentPrimeStack() {
        if (AbstractDungeon.player == null) return 0;
        return AbstractDungeon.player.hasPower(PrimeStackPower.POWER_ID)
                ? AbstractDungeon.player.getPower(PrimeStackPower.POWER_ID).amount
                : 0;
    }
    private static boolean isPrime(int n) {
        if (n < 2) return false;
        if (n % 2 == 0) return n == 2;
        for (int i = 3; i * i <= n; i += 2) if (n % i == 0) return false;
        return true;
    }
    private static int nextPrime(int n) {
        if (n < 2) return 2;
        int x = n + 1;
        while (!isPrime(x)) x++;
        return x;
    }
}
