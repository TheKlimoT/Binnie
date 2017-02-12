package binnie.genetics.integration.jei.database;

import binnie.genetics.integration.jei.GeneticsJeiPlugin;
import binnie.genetics.integration.jei.RecipeUids;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.client.Minecraft;

public class DatabaseRecipeCategory extends BlankRecipeCategory<DatabaseRecipeWrapper> {
	@Override
	public String getUid() {
		return RecipeUids.DATABASE;
	}

	@Override
	public String getTitle() {
		return "Genetic Database";
	}

	@Override
	public IDrawable getBackground() {
		return GeneticsJeiPlugin.guiHelper.createBlankDrawable(140, 75);
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
		IDrawable slot = GeneticsJeiPlugin.guiHelper.getSlotDrawable();
		slot.draw(minecraft, 30, 0);
		slot.draw(minecraft, 85, 0);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, DatabaseRecipeWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		itemStacks.init(0, true, 30, 0);
		itemStacks.init(1, true, 58, 18);
		itemStacks.init(2, false, 85, 0);

		itemStacks.set(ingredients);
	}
}
