package ar.edu.itba.sia.Ohn0;

public enum ResolveMethod {
    FILL_BLANKS {
        public String toString() {
            return "fill blanks";
        }
    },
    HEURISTIC_REPARATION {
        public String toString() {
            return "heuristic repair";
        }
    }
}
