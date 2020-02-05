package com.nyrds.android.lua;

/**
 * Created by mike on 16.06.2017.
 * This file is part of Remixed Pixel Dungeon.
 */

import com.nyrds.android.util.ModdingMode;
import com.nyrds.android.util.TrackedRuntimeException;
import com.nyrds.pixeldungeon.ml.EventCollector;

import org.apache.commons.io.input.BOMInputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.CoroutineLib;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.ResourceFinder;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseIoLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.lib.jse.JseOsLib;

import java.io.InputStream;

import lombok.var;

public class LuaEngine implements ResourceFinder {

	public static final String    SCRIPTS_LIB_STORAGE = "scripts/lib/storage";
    public static final String    LUA_DATA = "luaData";
	public static final LuaTable  emptyTable = new LuaTable();

    static private      LuaEngine engine              = new LuaEngine();

	private Globals globals;

	public static void reset() {
		engine = new LuaEngine();
	}

	public LuaValue call(String method) {
		return globals.get(method).call();
	}

	public LuaValue call(String method, Object arg1) {
		try {
			LuaValue methodForData = globals.get(method);
			return methodForData.call(CoerceJavaToLua.coerce(arg1));
		} catch (LuaError err) {
			reportLuaError(err);
		}
		return LuaValue.NIL;
	}

	@Nullable
	public static LuaTable moduleInstance(String module) {
		LuaValue luaModule = getEngine().call("dofile", module+".lua");

		if(luaModule.istable()) {
			return luaModule.checktable();
		}

		EventCollector.logException("failed to load instance of lua module: "+module);
		return null;
	}


	@Nullable
	public static LuaTable module(String module) {
		LuaValue luaModule = getEngine().call("require", module);

		if(luaModule.istable()) {
			return luaModule.checktable();
		}

		EventCollector.logException("failed to load lua module: "+module);

		return null;
	}


	private class resLoader extends OneArgFunction {
		public LuaValue call(LuaValue x) {
			return LuaValue.valueOf(ModdingMode.getResource(x.tojstring()));
		}
	}

	public static LuaEngine getEngine() {
		return engine;
	}

	private LuaEngine() {
		globals = new Globals();
		globals.load(new JseBaseLib());
		globals.load(new PackageLib());
		globals.load(new Bit32Lib());
		globals.load(new TableLib());
		globals.load(new StringLib());
		globals.load(new CoroutineLib());
		globals.load(new JseMathLib());
		globals.load(new JseIoLib());
		globals.load(new JseOsLib());
		globals.load(new MultiDexLuajavaLib());
		LoadState.install(globals);
		LuaC.install(globals);

		globals.finder = this;
		globals.set("loadResource", new resLoader());
	}

	private void reportLuaError(LuaError err) {
		throw new TrackedRuntimeException(err);
	}

	public LuaTable require(String module) {
		return module(module);
	}

	public void runScriptFile(@NotNull String fileName) {
		try {
			globals.loadfile(fileName).call();
		} catch (LuaError err) {
			reportLuaError(err);
		}
	}

	@Override
	public InputStream findResource(String filename) {
		return new BOMInputStream(ModdingMode.getInputStream(filename));
	}

	static public void forEach(@NotNull LuaTable tbl, LuaEntryAction action) {
		var k = LuaValue.NIL;
		while ( true ) {
			var n = tbl.next(k);
			if ( (k = n.arg1()).isnil() )
				break;
			action.apply(k, n.arg(2));
		}
	}
}
