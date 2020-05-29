package com.devs.group.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.devs.group.common.ssohandler.domain.entity.Member;
import com.devs.group.common.ssohandler.domain.entity.MemberProfile;
import com.devs.group.common.ssohandler.service.MemberService;
import com.devs.group.model.entity.GroupChannel;
import com.devs.group.model.vo.MemberJoinProfileSimpleVo;
import com.devs.group.service.GroupChannelService;
import com.devs.group.service.SideBarService;

@RestController
@RequestMapping("/group/*")
public class GroupController {

	private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

	@Autowired
	private MemberService memberService;

	@Autowired
	private SideBarService sideBarService;

	@Autowired
	private GroupChannelService groupChannelService;

	@Value("${server.port}")
	private int SERVER_PORT;

	@Value("${clientDomain}")
	private String CLIENT_DOMAIN;

	@Value("${clientSocketProtocol}")
	private String CLIENT_SOCKET_PROTOCOL;

	@Value("${clientProtocol}")
	private String CLIENT_PROTOCOL;

	// nameSearchAutoComplete
	@PostMapping(value = "nameSearchAutoComplete")
	public List<MemberJoinProfileSimpleVo> nameSearchAutoComplete(int my_member_code, String id_name) {
		logger.info("DM/nameSearchAutoComplete.POST");
		return memberService.nameSearchAutoComplete(my_member_code, id_name);
	}

	/*
	 * 초기 입장 페이지 group.jsp 로 이동 (MainController 를 통해 redirect받음)
	 */
	@GetMapping(value = "")
	public ModelAndView groupMainPage(HttpSession session, ModelMap map) {

		logger.info("GROUP PAGE");

		Member sessionMember = (Member) session.getAttribute("user");
		System.out.println("\n## user in session : " + sessionMember);

		if (sessionMember == null) {
			System.out.println("------------session이 null이잖아요~~~~?");
			return new ModelAndView("redirect:/");
		}

		Member dbMember = memberService.getUser(sessionMember.getMemberid());

		if (dbMember.getTokenId() == null) {

			System.out.println("------------session은 null이 아닌데 token id가 null이라서 세션에셔 user 까잖아요~~~?");
			session.removeAttribute("user");
			return new ModelAndView("redirect:/");

		} else {

			System.out.println("------------세션도 있고 token도 null이 아니에요~~~~~~");
			map.put("user", dbMember);

			// 프로필 정보 session에 셋팅
			session.setAttribute("profile", memberService.getMemberProfile(dbMember.getMembercode()));

			// properties를 session에 셋팅하여 jsp 페이지에서 사용한다.
			session.setAttribute("SERVER_PORT", SERVER_PORT);
			session.setAttribute("CLIENT_DOMAIN", CLIENT_DOMAIN);
			session.setAttribute("CLIENT_SOCKET_PROTOCOL", CLIENT_SOCKET_PROTOCOL);
			session.setAttribute("CLIENT_PROTOCOL", CLIENT_PROTOCOL);
		}

		return new ModelAndView("group");
	}

	/*
	 * 내 프로필 이미지 수정
	 */
	@PostMapping(value = "/updatememberprofileimage")
	private Map<String, Object> updateProfileImage(@RequestParam("memberImgOriginalName") MultipartFile multi,
			HttpServletRequest request, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();

		// 업로드될 경로
		String filePath = "/resources/images/profileupload/";

		// 업로드될 실제 경로 (이클립스 상의 절대경로)
		String FILE_PATH = session.getServletContext().getRealPath(filePath);
		System.out.println("절대경로 : " + FILE_PATH);

		// 디렉토리 없을 시 자동 생성!
		File file;
		if (!(file = new File(FILE_PATH)).isDirectory()) {
			file.mkdirs();
		}

		MemberProfile member_profile = new MemberProfile();

		String uuid = UUID.randomUUID().toString(); // 파일 랜덤번호 생성

		// 파라미터 받기
		int member_code = ((Member) session.getAttribute("user")).getMembercode();
		// 파일 첨부
		String MEMBER_IMG_SERVER_NAME = null;
		String MEMBER_IMG_ORIGINAL_NAME = null;
		String imgExtend = null;

		// 실제 저장된 파일명

		// 원래 이미지 이름
		MEMBER_IMG_ORIGINAL_NAME = multi.getOriginalFilename();
		System.out.println("original : " + MEMBER_IMG_ORIGINAL_NAME);

		if (MEMBER_IMG_ORIGINAL_NAME != null) {

			MEMBER_IMG_SERVER_NAME = uuid + MEMBER_IMG_ORIGINAL_NAME;

			// 이미지 확장자
			imgExtend = MEMBER_IMG_SERVER_NAME.substring(MEMBER_IMG_SERVER_NAME.lastIndexOf(".") + 1);
			System.out.println("이미지 확장자명 : " + imgExtend);

			File targetFile = new File(FILE_PATH, MEMBER_IMG_SERVER_NAME);

			try {
				InputStream fileStream = multi.getInputStream();
				FileUtils.copyInputStreamToFile(fileStream, targetFile);
			} catch (IOException e) {
				FileUtils.deleteQuietly(targetFile);
				e.printStackTrace();
			}

			member_profile.setMembercode(member_code);
			member_profile.setMemberImgOriginalName(MEMBER_IMG_ORIGINAL_NAME);
			member_profile.setMemberImgServerName(MEMBER_IMG_SERVER_NAME);
			member_profile.setMemberImgPath(FILE_PATH);
		}

		MemberProfile changedMemberProfile = memberService.updateMemberProfileImage(member_code, member_profile);

		// 프로필 정보를 session에 리셋
		session.setAttribute("profile", changedMemberProfile);
		System.out.println(changedMemberProfile);

		map.put("img", memberService.getMemberProfile(member_code).getMemberImgServerName());
		return map;
	}

	/*
	 * 내가만든 그룹채널 리스트
	 */
	@PostMapping("selectMyGroupChannelList")
	public List<GroupChannel> selectMyGroupChannelList(@RequestBody Map<String, Integer> map) {
		int membercode = map.get("membercode");

		return sideBarService.selectMyGroupChannelList(membercode);
	}

	/*
	 * 팔로우한 그룹채널 리스트
	 */
	@PostMapping("selectFollowGroupChannelList")
	public List<GroupChannel> selectFollowGroupChannelList(@RequestBody Map<String, Integer> map) {
		int membercode = map.get("membercode");

		return sideBarService.selectFollowGroupChannelList(membercode);
	}

	/*
	 * 선택된 채널 페이지로 이동 (MainController를 통해 redirect 받아 channel정보가 session에 담겨있음)
	 */
	@GetMapping(value = "channel")
	public ModelAndView channelMainPage(HttpSession session, @RequestParam("channelcode") int channelcode) {

		logger.info("{}번 채널 눌렀따", channelcode);
		
		session.setAttribute("channel", sideBarService.selectChannel(channelcode));
		session.setAttribute("follow",
				sideBarService.selectGroupFollow(channelcode, ((Member) session.getAttribute("user")).getMembercode()));

		System.out.println(((GroupChannel) session.getAttribute("channel")).getChannelcode() + " 번 그룹채널 입장!!!!");

		return new ModelAndView("channel");
	}

	/*
	 * 그룹 채널 만들기 process
	 */
	@PostMapping(value = "createGroupChannel")
	public ModelAndView createGroupChannel(GroupChannel groupChannel) {
		GroupChannel savedGroupChannel = groupChannelService.createGroupChannel(groupChannel);

		System.out.println("채널 생성 : " + savedGroupChannel);

		return new ModelAndView("redirect:/?channelcode=" + savedGroupChannel.getChannelcode());
	}

	/*
	 * 그룹채널 정보 수정
	 */
	@PostMapping(value = "updategroupchanneldescription")
	public ModelAndView updateGroupChannelDescription(HttpSession session, GroupChannel newGroupChannel) {

		int channelcode = ((GroupChannel) session.getAttribute("channel")).getChannelcode();
		System.out.println(channelcode + " 번 채널 정보 수정!");

		GroupChannel changedGroupChannel = groupChannelService.updateGroupChannelDescription(channelcode,
				newGroupChannel);
		session.setAttribute("channel", changedGroupChannel);

		return new ModelAndView("redirect:/group/channel");
	}

	/*
	 * 그룹채널 이미지 수정
	 */
	@PostMapping(value = "updategroupchannelimage")
	private Map<String, Object> updateGroupChannelImage(@RequestParam("channelimgoriginalname") MultipartFile multi,
			HttpServletRequest request, HttpSession session) {
		Map<String, Object> map = new HashMap<String, Object>();

		// 업로드될 경로
		String filePath = "/resources/images/groupchannelprofileupload/";

		// 업로드될 실제 경로 (이클립스 상의 절대경로)
		String FILE_PATH = session.getServletContext().getRealPath(filePath);
		System.out.println("절대경로 : " + FILE_PATH);

		// 디렉토리 없을 시 자동 생성!
		File file;
		if (!(file = new File(FILE_PATH)).isDirectory()) {
			file.mkdirs();
		}

		// 파라미터 받기
		int channelcode = ((GroupChannel) session.getAttribute("channel")).getChannelcode();

		// 이미지 이외의 값이 변경되는것을 막기위해 db에서 기존 채널 정보를 가져옴
		GroupChannel newGroupChannel = new GroupChannel();

		String uuid = UUID.randomUUID().toString(); // 파일 랜덤번호 생성

		// 파일 첨부
		String channelimgservername = null;
		String channelimgoriginalname = null;
		String imgExtend = null;

		// 원래 이미지 이름
		channelimgoriginalname = multi.getOriginalFilename();
		System.out.println("original : " + channelimgoriginalname);

		if (channelimgoriginalname != null) {

			channelimgservername = uuid + channelimgoriginalname;

			// 이미지 확장자
			imgExtend = channelimgservername.substring(channelimgservername.lastIndexOf(".") + 1);
			System.out.println("이미지 확장자명 : " + imgExtend);

			File targetFile = new File(FILE_PATH, channelimgservername);

			try {
				InputStream fileStream = multi.getInputStream();
				FileUtils.copyInputStreamToFile(fileStream, targetFile);
			} catch (IOException e) {
				FileUtils.deleteQuietly(targetFile);
				e.printStackTrace();
			}

			newGroupChannel.setChannelcode(channelcode);
			newGroupChannel.setChannelimgoriginalname(channelimgoriginalname);
			newGroupChannel.setChannelimgservername(channelimgservername);
			newGroupChannel.setChannelimgpath(FILE_PATH);
		}

		GroupChannel changedGroupChannel = groupChannelService.updateGroupChannelImage(channelcode, newGroupChannel);

		// 프로필 정보를 session에 리셋
		session.setAttribute("channel", changedGroupChannel);
		System.out.println(changedGroupChannel);

		map.put("img", changedGroupChannel.getChannelimgservername());
		return map;
	}

	/*
	 * 그룹 참여자 리스트
	 */
	@PostMapping("selectFollowerRoleEditer")
	public List<MemberJoinProfileSimpleVo> selectFollowerRoleEditer(@RequestBody Map<String, Integer> map) {
		int channelcode = map.get("channelcode");

		return sideBarService.selectFollowerRoleEditor(channelcode);
	}

	@PostMapping("selectFollowerRoleReader")
	public List<MemberJoinProfileSimpleVo> selectFollowerRoleReader(@RequestBody Map<String, Integer> map) {
		int channelcode = map.get("channelcode");

		return sideBarService.selectFollowerRoleReader(channelcode);
	}
}
