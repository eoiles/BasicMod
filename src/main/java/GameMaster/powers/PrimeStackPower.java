package GameMaster.powers;

import static GameMaster.BasicMod.makeID;

public class PrimeStackPower extends BasePower {
    public static final String POWER_ID = makeID("PrimeStack"); // packless id

    public PrimeStackPower(com.megacrit.cardcrawl.core.AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void updateDescription() {
        int next = nextPrime(Math.max(0, this.amount));
        this.description = "Next prime number is " + next + ".";
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8f;
        this.amount = Math.max(0, this.amount + stackAmount);
        updateDescription();
    }

    // ---- helpers ----
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
