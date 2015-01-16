package reference;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Freek on 6/01/2015.
 */
public class Textures {

    public static String TEXTURE_TOXSICK_LOGO = "textures/toxsick_logo.png";

    public static String TEXTURE_UI_WOOD = "textures/ui/wood_ui.png";
    public static String TEXTURE_UI_STONE = "textures/ui/stone_ui.png";
    public static String TEXTURE_UI_INTERACT = "textures/ui/button_interact.png";
    public static String TEXTURE_UI_PLACE = "textures/ui/button_place.png";


    private static String[] textures = {
            TEXTURE_UI_WOOD,
            TEXTURE_UI_STONE,
            TEXTURE_UI_INTERACT,
            TEXTURE_UI_PLACE
    };
    public static Array<String> TEXTURES = new Array<String>(textures);

}
