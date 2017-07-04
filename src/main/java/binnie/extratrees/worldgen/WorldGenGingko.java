package binnie.extratrees.worldgen;

import forestry.api.world.ITreeGenData;

public class WorldGenGingko extends WorldGenTree {
	public WorldGenGingko(ITreeGenData tree) {
		super(tree);
	}

	@Override
	public void generate() {
		generateTreeTrunk(height, girth);
		float leafSpawn = height + 2;
		float bottom = 2.0f;
		float width = height * randBetween(0.55f, 0.6f);
		if (width > 7.0f) {
			width = 7.0f;
		}

		generateCylinder(new Vector(0.0f, leafSpawn--, 0.0f), width - 2.0f, 1, leaf, false);

		while (leafSpawn > bottom * 2.0f + 1.0f) {
			generateCylinder(new Vector(0.0f, leafSpawn--, 0.0f), width - 1.5f, 1, leaf, false);
			generateCylinder(new Vector(0.0f, leafSpawn--, 0.0f), width - 0.9f, 1, leaf, false);
		}

		generateCylinder(new Vector(0.0f, leafSpawn--, 0.0f), width, 1, leaf, false);

		while (leafSpawn > bottom) {
			generateCylinder(new Vector(0.0f, leafSpawn--, 0.0f), width - 0.9f, 1, leaf, false);
			generateCylinder(new Vector(0.0f, leafSpawn--, 0.0f), width, 1, leaf, false);
		}

		generateCylinder(new Vector(0.0f, leafSpawn, 0.0f), 0.7f * width, 1, leaf, false);
	}

	@Override
	public void preGenerate() {
		height = determineHeight(6, 2);
		girth = determineGirth(treeGen.getGirth(world, startX, startY, startZ));
	}
}
