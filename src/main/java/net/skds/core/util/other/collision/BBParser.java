package net.skds.core.util.other.collision;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.skds.core.SKDSCore;
import net.skds.core.util.JarFileReader;
import net.skds.core.util.mat.Matrix3;
import net.skds.core.util.mat.Quat;
import net.skds.core.util.mat.Vec3;
import net.skds.lonely.Lonely;

@OnlyIn(Dist.CLIENT)
public class BBParser {

	private static Map<String, List<OBB>> boxes = new HashMap<>();
	private static final List<OBB> empty;

	public static List<OBB> get(String key) {
		List<OBB> list = boxes.get(key);
		if (list == null) {
			return empty;
		}
		return list;
	}

	public static void read() {
		JarFileReader jfr = new JarFileReader("obbshapes");
		try {
			jfr.read();
		} catch (IOException e) {
			SKDSCore.LOGGER.error(e);
		}
		boxes.clear();
		for (String path : jfr.files) {
			readJson(path);
		}

	}

	private static void readJson(String path) {

		JsonObject jsonobject = new JsonObject();
		try {
			InputStream is = BBParser.class.getClassLoader().getResourceAsStream(path);
			if (is == null) {

				System.out.println("Pizdec " + path);
				return;
			}
			Reader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
			JsonReader jsonReader = new JsonReader(r);
			Gson GSON = new Gson();
			jsonobject = GSON.getAdapter(JsonObject.class).read(jsonReader);

			String shortPath = path.replace("obbshapes/", "");

			boxes.put(shortPath, parseBBM(jsonobject));

			Lonely.LOGGER.info("Loaded BBM " + shortPath);
		} catch (IOException e) {
			Lonely.LOGGER.error("BBM Loading error " + path);
		}
	}

	private static List<OBB> parseBBM(JsonObject jsonobject) {
		List<OBB> list = new ArrayList<>();

		jsonobject.get("elements").getAsJsonArray().forEach(e -> {
			JsonObject jo = e.getAsJsonObject();
			String name = jo.get("name").getAsString();
			if (name.startsWith("#")) {
				return;
			}
			JsonArray from = jo.get("from").getAsJsonArray();
			JsonArray to = jo.get("to").getAsJsonArray();
			JsonArray origin = jo.get("origin").getAsJsonArray();
			double fromX = from.get(0).getAsDouble();
			double fromY = from.get(1).getAsDouble();
			double fromZ = from.get(2).getAsDouble();
			double toX = to.get(0).getAsDouble();
			double toY = to.get(1).getAsDouble();
			double toZ = to.get(2).getAsDouble();
			AxisAlignedBB aabb = new AxisAlignedBB(fromX / 16, fromY / 16, fromZ / 16, toX / 16, toY / 16, toZ / 16);

			Matrix3 matrix3;
			if (jo.has("rotation")) {
				JsonArray rotation = jo.get("rotation").getAsJsonArray();
				Quat q = new Quat(Vec3.ZP, rotation.get(2).getAsDouble(), true);
				q.multiply(new Quat(Vec3.YP, rotation.get(1).getAsDouble(), true));
				q.multiply(new Quat(Vec3.XP, rotation.get(0).getAsDouble(), true));
				matrix3 = new Matrix3(q);
			} else {
				matrix3 = new Matrix3();
			}
			Vec3 center = new Vec3(origin.get(0).getAsDouble() / 16, origin.get(1).getAsDouble() / 16,
					origin.get(2).getAsDouble() / 16);

			list.add(OBB.create(aabb, matrix3, center, name).offset(new Vec3(-0.5, -0.5, -0.5)));

		});

		return list;
	}

	static {
		empty = new ArrayList<>();
		AxisAlignedBB aabb = new AxisAlignedBB(-.5, -.5, -.5, .5, .5, .5);
		OBB obb = OBB.create(aabb);
		empty.add(obb);
	}

}
