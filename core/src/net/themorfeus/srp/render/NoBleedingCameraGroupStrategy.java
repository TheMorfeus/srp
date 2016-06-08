/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package net.themorfeus.srp.render;

import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalMaterial;
import com.badlogic.gdx.graphics.g3d.decals.GroupStrategy;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Pool;


/**
 * Camera group strategy that disables blending, which causes severe texture bleeding.
 * */
public class NoBleedingCameraGroupStrategy extends CameraGroupStrategy{
	public NoBleedingCameraGroupStrategy(Camera camera) {
		super(camera);
	}

	public NoBleedingCameraGroupStrategy(Camera camera, Comparator<Decal> sorter) {
		super(camera, sorter);
	}

	@Override
	public void beforeGroups () {
		getGroupShader(0).begin();
		getGroupShader(0).setUniformMatrix("u_projectionViewMatrix", getCamera().combined);
		getGroupShader(0).setUniformi("u_texture", 0);
	}
}
