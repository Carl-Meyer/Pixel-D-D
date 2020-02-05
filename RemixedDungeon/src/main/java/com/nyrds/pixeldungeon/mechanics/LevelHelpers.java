package com.nyrds.pixeldungeon.mechanics;

import com.nyrds.android.util.Util;
import com.nyrds.pixeldungeon.ml.EventCollector;
import com.watabou.pixeldungeon.actors.Char;
import com.watabou.pixeldungeon.levels.Level;

public class LevelHelpers {
    static public int pushDst(Char chr, HasPositionOnLevel obj, boolean onlyRect) {

        Level level = chr.level();

        int hx = level.cellX(chr.getPos());
        int hy = level.cellY(chr.getPos());

        int x = level.cellX(obj.getPos());
        int y = level.cellY(obj.getPos());

        int dx = x - hx;
        int dy = y - hy;

        if(dx==0 && dy == 0) {
            EventCollector.logException("push from same position?");
            return Level.INVALID_CELL;
        }

        if (onlyRect && dx * dy != 0) {
            return Level.INVALID_CELL;
        }

        int nextCell = level.cell(x + Util.signum(dx), y + Util.signum(dy));

        if(!level.cellValid(nextCell)) {
            return Level.INVALID_CELL;
        }

        if(level.solid[nextCell]) {
            return Level.INVALID_CELL;
        }

        return nextCell;
    }
}
