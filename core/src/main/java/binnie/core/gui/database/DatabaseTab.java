package binnie.core.gui.database;

import binnie.core.AbstractMod;
import binnie.core.gui.ITooltipHelp;
import binnie.core.gui.Tooltip;
import binnie.core.util.I18N;

public class DatabaseTab implements ITooltipHelp {
	private String modId;
	private String unloc;

	public DatabaseTab(final AbstractMod mod, final String unloc) {
		this.modId = mod.getModId();
		this.unloc = unloc;
	}

	public DatabaseTab(String modId, String unloc) {
		this.modId = modId;
		this.unloc = unloc;
	}

	@Override
	public String toString() {
		return I18N.localise(this.modId + ".gui.database.tab." + this.unloc);
	}

	@Override
	public void getHelpTooltip(final Tooltip tooltip) {
		tooltip.add(I18N.localiseOrBlank(this.modId + ".gui.database.tab." + this.unloc + ".help"));
	}
}