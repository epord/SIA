package ar.edu.itba.sia.Warriors;

public enum WarriorType {
    SOLDIER {
        @Override
        public double getAttackFactor() {
            return 0.6;
        }

        @Override
        public double getDefenseFactor() {
            return 0.4;
        }
    },
    ARCHER {
        @Override
        public double getAttackFactor() {
            return 0.9;
        }

        @Override
        public double getDefenseFactor() {
            return 0.1;
        }
    },
    DEFENSOR {
        @Override
        public double getAttackFactor() {
            return 0.1;
        }

        @Override
        public double getDefenseFactor() {
            return 0.9;
        }
    },
    ASSASIN {
        @Override
        public double getAttackFactor() {
            return 0.7;
        }

        @Override
        public double getDefenseFactor() {
            return 0.3;
        }
    };

    public abstract double getAttackFactor();
    public abstract double getDefenseFactor();

}
