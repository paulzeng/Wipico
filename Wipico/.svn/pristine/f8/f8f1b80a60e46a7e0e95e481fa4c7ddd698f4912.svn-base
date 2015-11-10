package com.chinasvc.wipico.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;

import android.content.Context;

/**
 * 文件操作类
 * 
 * @since 1.0.0
 * */
public class FileUtil {

	private Context mContext;

	/** 类的实例化 */
	private static FileUtil instance;

	/**
	 * 构造 FileUtil实例化对象
	 * */
	public FileUtil(Context context) {
		mContext = context;
	}

	public static boolean isFileExist(String path) {
		File file = new File(path);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * .ppt|.pps|.pptx|.doc|.docx|.xls|.xlsx|.pdf
	 * 
	 * */
	public static boolean isOfficeFile(String fileName) {
		if (fileName.equalsIgnoreCase("ppt") || fileName.equalsIgnoreCase("pps") || fileName.equalsIgnoreCase("pptx") || fileName.equalsIgnoreCase("doc") || fileName.equalsIgnoreCase("docx")
				|| fileName.equalsIgnoreCase("xls") || fileName.equalsIgnoreCase("xlsx") || fileName.equalsIgnoreCase("pdf")) {
			return true;
		} else {
			return false;
		}
	}

	private static final HashSet<String> mHashImage;
	private static final HashSet<String> mHashVideo;
	private static final HashSet<String> mHashAudio;

	public static final String[] IMAGE_EXTENSIONS = { "png", "jpeg", "jpg" };

	public static final String[] VIDEO_EXTENSIONS = { "264", "3g2", "3gp", "3gp2", "3gpp", "3gpp2", "3mm", "3p2", "60d", "aep", "ajp", "amv", "amx", "arf", "asf", "asx", "avb", "avd", "avi",
			"avs", "avs", "axm", "bdm", "bdmv", "bik", "bix", "bmk", "box", "bs4", "bsf", "byu", "camre", "clpi", "cpi", "cvc", "d2v", "d3v", "dav", "dce", "dck", "ddat", "dif", "dir",
			"divx", "dlx", "dmb", "dmsm", "dmss", "dnc", "dpg", "dream", "dsy", "dv", "dv-avi", "dv4", "dvdmedia", "dvr-ms", "dvx", "dxr", "dzm", "dzp", "dzt", "evo", "eye", "f4p", "f4v",
			"fbr", "fbr", "fbz", "fcp", "flc", "flh", "fli", "flv", "flx", "gl", "grasp", "gts", "gvi", "gvp", "hdmov", "hkm", "ifo", "imovi", "imovi", "iva", "ivf", "ivr", "ivs", "izz",
			"izzy", "jts", "lsf", "lsx", "m15", "m1pg", "m1v", "m21", "m21", "m2a", "m2p", "m2t", "m2ts", "m2v", "m4e", "m4u", "m4v", "m75", "meta", "mgv", "mj2", "mjp", "mjpg", "mkv",
			"mmv", "mnv", "mod", "modd", "moff", "moi", "moov", "mov", "movie", "mp21", "mp21", "mp2v", "mp4", "mp4v", "mpe", "mpeg", "mpeg4", "mpf", "mpg", "mpg2", "mpgin", "mpl",
			"mpls", "mpv", "mpv2", "mqv", "msdvd", "msh", "mswmm", "mts", "mtv", "mvb", "mvc", "mvd", "mve", "mvp", "mxf", "mys", "ncor", "nsv", "nvc", "ogm", "ogv", "ogx", "osp", "par",
			"pds", "pgi", "piv", "playlist", "pmf", "prel", "pro", "prproj", "psh", "pva", "pvr", "pxv", "qt", "qtch", "qtl", "qtm", "qtz", "rcproject", "rdb", "rec", "rm", "rmd", "rmp",
			"rmvb", "roq", "rp", "rts", "rts", "rum", "rv", "sbk", "sbt", "scm", "scm", "scn", "sec", "seq", "sfvidcap", "smil", "smk", "sml", "smv", "spl", "ssm", "str", "stx", "svi",
			"swf", "swi", "swt", "tda3mt", "tivo", "tix", "tod", "tp", "tp0", "tpd", "tpr", "trp", "ts", "tvs", "vc1", "vcr", "vcv", "vdo", "vdr", "veg", "vem", "vf", "vfw", "vfz", "vgz",
			"vid", "viewlet", "viv", "vivo", "vlab", "vob", "vp3", "vp6", "vp7", "vpj", "vro", "vsp", "w32", "wcp", "webm", "wm", "wmd", "wmmp", "wmv", "wmx", "wp3", "wpl", "wtv", "wvx",
			"xfl", "xvid", "yuv", "zm1", "zm2", "zm3", "zmv" };

	public static final String[] AUDIO_EXTENSIONS = { "4mp", "669", "6cm", "8cm", "8med", "8svx", "a2m", "aa", "aa3", "aac", "aax", "abc", "abm", "ac3", "acd", "acd-bak", "acd-zip", "acm", "act",
			"adg", "afc", "agm", "ahx", "aif", "aifc", "aiff", "ais", "akp", "al", "alaw", "all", "amf", "amr", "ams", "ams", "aob", "ape", "apf", "apl", "ase", "at3", "atrac", "au",
			"aud", "aup", "avr", "awb", "band", "bap", "bdd", "box", "bun", "bwf", "c01", "caf", "cda", "cdda", "cdr", "cel", "cfa", "cidb", "cmf", "copy", "cpr", "cpt", "csh", "cwp",
			"d00", "d01", "dcf", "dcm", "dct", "ddt", "dewf", "df2", "dfc", "dig", "dig", "dls", "dm", "dmf", "dmsa", "dmse", "drg", "dsf", "dsm", "dsp", "dss", "dtm", "dts", "dtshd",
			"dvf", "dwd", "ear", "efa", "efe", "efk", "efq", "efs", "efv", "emd", "emp", "emx", "esps", "f2r", "f32", "f3r", "f4a", "f64", "far", "fff", "flac", "flp", "fls", "frg",
			"fsm", "fzb", "fzf", "fzv", "g721", "g723", "g726", "gig", "gp5", "gpk", "gsm", "gsm", "h0", "hdp", "hma", "hsb", "ics", "iff", "imf", "imp", "ins", "ins", "it", "iti", "its",
			"jam", "k25", "k26", "kar", "kin", "kit", "kmp", "koz", "koz", "kpl", "krz", "ksc", "ksf", "kt2", "kt3", "ktp", "l", "la", "lqt", "lso", "lvp", "lwv", "m1a", "m3u", "m4a",
			"m4b", "m4p", "m4r", "ma1", "mdl", "med", "mgv", "midi", "miniusf", "mka", "mlp", "mmf", "mmm", "mmp", "mo3", "mod", "mp1", "mp2", "mp3", "mpa", "mpc", "mpga", "mpu", "mp_",
			"mscx", "mscz", "msv", "mt2", "mt9", "mte", "mti", "mtm", "mtp", "mts", "mus", "mws", "mxl", "mzp", "nap", "nki", "nra", "nrt", "nsa", "nsf", "nst", "ntn", "nvf", "nwc",
			"odm", "oga", "ogg", "okt", "oma", "omf", "omg", "omx", "ots", "ove", "ovw", "pac", "pat", "pbf", "pca", "pcast", "pcg", "pcm", "peak", "phy", "pk", "pla", "pls", "pna",
			"ppc", "ppcx", "prg", "prg", "psf", "psm", "ptf", "ptm", "pts", "pvc", "qcp", "r", "r1m", "ra", "ram", "raw", "rax", "rbs", "rcy", "rex", "rfl", "rmf", "rmi", "rmj", "rmm",
			"rmx", "rng", "rns", "rol", "rsn", "rso", "rti", "rtm", "rts", "rvx", "rx2", "s3i", "s3m", "s3z", "saf", "sam", "sb", "sbg", "sbi", "sbk", "sc2", "sd", "sd", "sd2", "sd2f",
			"sdat", "sdii", "sds", "sdt", "sdx", "seg", "seq", "ses", "sf2", "sfk", "sfl", "shn", "sib", "sid", "sid", "smf", "smp", "snd", "snd", "snd", "sng", "sng", "sou", "sppack",
			"sprg", "sseq", "sseq", "ssnd", "stm", "stx", "sty", "svx", "sw", "swa", "syh", "syw", "syx", "td0", "tfmx", "thx", "toc", "tsp", "txw", "u", "ub", "ulaw", "ult", "ulw",
			"uni", "usf", "usflib", "uw", "uwf", "vag", "val", "vc3", "vmd", "vmf", "vmf", "voc", "voi", "vox", "vpm", "vqf", "vrf", "vyf", "w01", "wav", "wav", "wave", "wax", "wfb",
			"wfd", "wfp", "wma", "wow", "wpk", "wproj", "wrk", "wus", "wut", "wv", "wvc", "wve", "wwu", "xa", "xa", "xfs", "xi", "xm", "xmf", "xmi", "xmz", "xp", "xrns", "xsb", "xspf",
			"xt", "xwb", "ym", "zvd", "zvr" };

	static {
		mHashImage = new HashSet<String>(Arrays.asList(IMAGE_EXTENSIONS));
		mHashVideo = new HashSet<String>(Arrays.asList(VIDEO_EXTENSIONS));
		mHashAudio = new HashSet<String>(Arrays.asList(AUDIO_EXTENSIONS));
	}

	/**
	 * 是否是图片
	 * 
	 * @param f
	 *                File
	 * */
	public static boolean isPicture(String ext) {
		return mHashImage.contains(ext);
	}

	/**
	 * 是否是视频
	 * 
	 * @param f
	 *                File
	 * */
	public static boolean isVideo(String ext) {
		return mHashVideo.contains(ext);
	}

	/**
	 * 是否是音频
	 * 
	 * @param f
	 *                File
	 * */
	public static boolean isAudio(String ext) {
		return mHashAudio.contains(ext);
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param f
	 *                File
	 * */
	public static String getFileExtension(File f) {
		if (f != null) {
			String filename = f.getName();
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1) {
				return filename.substring(i + 1).toLowerCase();
			}
		}
		return null;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param f
	 *                File
	 * */
	public static String getFileExtension(String filename) {
		if (filename != null) {
			int i = filename.lastIndexOf('.');
			if (i > 0 && i < filename.length() - 1) {
				return filename.substring(i + 1).toLowerCase();
			}
		}
		return null;
	}

	/**
	 * 返回当前类的实例化对象
	 * 
	 * @param context
	 * */
	public static FileUtil getInstance(Context context) {
		if (instance == null) {
			instance = new FileUtil(context);
		}
		return instance;
	}

	/**
	 * 拷贝项目资源文件到目标路径文件中
	 * 
	 * @param resourceId
	 *                项目资源文件
	 * 
	 * @param targetFile
	 *                目标文件
	 * 
	 * @exception IOException
	 * */
	public void copyResourceFile(int resourceId, String targetFile) throws IOException {
		InputStream fin = mContext.getResources().openRawResource(resourceId);
		FileOutputStream fos = new FileOutputStream(targetFile);
		int length;
		byte[] buffer = new byte[1024 * 32];
		while ((length = fin.read(buffer)) != -1) {
			fos.write(buffer, 0, length);
		}
		fin.close();
		fos.close();
	}

}
