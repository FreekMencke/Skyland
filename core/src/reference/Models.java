package reference;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Freek on 6/01/2015.
 */
public class Models {

    public static String MODEL_ISLAND_PROTOTYPE = "models/island_prototype.g3dj";
    public static String MODEL_CLOUD = "models/cloud_prototype.g3dj";
    public static String MODEL_TREE_PROTOTYPE = "models/tree/tree_prototype.g3dj";
    public static String MODEL_STUMP_PROTOTYPE = "models/tree/stump_prototype.g3dj";
    public static String MODEL_LOG_PROTOTYPE = "models/tree/log_prototype.g3dj";
    public static String MODEL_CAVE_PROTOTYPE = "models/cave_prototype.g3dj";
    public static String MODEL_STONE_PROTOTYPE = "models/stone_prototype.g3dj";


    private static String[] models = {
            MODEL_ISLAND_PROTOTYPE,
            MODEL_CLOUD,
            MODEL_STUMP_PROTOTYPE,
            MODEL_TREE_PROTOTYPE,
            MODEL_LOG_PROTOTYPE,
            MODEL_CAVE_PROTOTYPE,
            MODEL_STONE_PROTOTYPE
    };
    public static Array<String> MODELS = new Array<String>(models);
}
