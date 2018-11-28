package seecov.lib.patch;

public class PatchFactory {
	
	public static Patch CreatePatchContext(String patchFilename, int patchType) {
		switch (patchType) {
			case Patch.PATCH_TYPE_UNIFIED_GIT:
				return new GitPatch(patchFilename);
			default:
				return null;
		}
	}
	
}
