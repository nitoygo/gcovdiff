package seecov.lib.patch;

public abstract class Patch implements PatchParser {
	
	public final static int PATCH_TYPE_UNIFIED_GIT = 0x01;
	public final static int PATCH_TYPE_UNIFIED_BC  = 0x02;
	public final static int PATCH_TYPE_NORMAL_BC   = 0x03;
	
	public String patchFilename;
	
	public Patch(String patchFilename) {
		this.patchFilename = patchFilename;
	}
}
