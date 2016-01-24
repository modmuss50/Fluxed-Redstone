package me.modmuss50.fr;

import me.modmuss50.fr.mutlipart.MultipartPipe;
import me.modmuss50.fr.mutlipart.types.BlazePipe;
import me.modmuss50.fr.mutlipart.types.EnderPipe;
import me.modmuss50.fr.mutlipart.types.GoldPipe;
import net.minecraft.util.IStringSerializable;

/**
 * Created by mark on 06/01/2016.
 */
public enum PipeTypeEnum implements IStringSerializable {

	REDSTONE(128, "Redstone", "fluxedredstone:blocks/cable_redstone", 6.0, MultipartPipe.class),
	GOLD(128 * 8, "Gold", "fluxedredstone:blocks/cable_gold", 6.0, GoldPipe.class),
	BALZE(128 * 8 * 8, "Blaze", "fluxedredstone:blocks/cable_blaze", 6.0, BlazePipe.class),
	ENDER(128 * 8 * 8 * 8, "Ender", "fluxedredstone:blocks/cable_ender", 5.0, EnderPipe.class);

	private int maxRF;
	private int defualtRF;
	private String friendlyName;
	private String textureName;
	private Double thickness;
	private Class<MultipartPipe> classType;

	PipeTypeEnum(int maxRF, String friendlyName, String textureName, Double thickness, Class multipartPipeClass) {
		this.maxRF = maxRF;
		this.friendlyName = friendlyName;
		this.textureName = textureName;
		this.thickness = thickness;
		this.classType = multipartPipeClass;
		this.defualtRF = maxRF;
	}

	@Override
	public String getName() {
		return friendlyName.toLowerCase();
	}

	public int getMaxRF() {
		return maxRF;
	}

	public void setMaxRF(int maxRF) {
		this.maxRF = maxRF;
	}

	public int getDefualtRF() {
		return defualtRF;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

	public String getTextureName() {
		return textureName;
	}

	public Double getThickness() {
		return thickness;
	}

	public Class<MultipartPipe> getClassType() {
		return classType;
	}
}
