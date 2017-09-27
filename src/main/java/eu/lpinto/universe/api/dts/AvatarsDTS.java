/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.lpinto.universe.api.dts;

import eu.lpinto.universe.persistence.entities.Image;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Luis Pinto <code>- mail@lpinto.eu</code>
 */
public class AvatarsDTS {

    public static List<String> urls(final List<Image> images) {
        if (images == null) {
            return null;
        }

        List<String> result = new ArrayList<>(images.size());
        for (Image image : images) {
            result.add(image.getUrl());
        }

        return result;
    }
}
