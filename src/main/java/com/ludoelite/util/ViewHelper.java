package com.ludoelite.util;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Helper class for view-related utilities.
 */
public class ViewHelper {

    /**
     * Load icon from resources.
     */
    public static Node loadIcon(String iconName) {
        try {
            // Try to load image
            String path = "/icons/" + iconName;
            Image image = new Image(ViewHelper.class.getResourceAsStream(path));
            return new ImageView(image);
        } catch (Exception e) {
            // Return empty node if icon not found
            return new javafx.scene.layout.Pane();
        }
    }
}