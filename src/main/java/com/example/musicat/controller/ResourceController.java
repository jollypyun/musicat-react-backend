package com.example.musicat.controller;

import com.example.musicat.domain.member.GradeResourceVO;
import com.example.musicat.domain.member.GradeVO;
import com.example.musicat.domain.member.ResourceVO;
import com.example.musicat.mapper.member.ResourceMapper;
import com.example.musicat.service.member.GradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class ResourceController {

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private GradeService gradeService;

    //리소스 + 권한 목록 조회
    @GetMapping("/resourceManager")
    public String resourceManager(Model model) throws Exception {
        List<GradeResourceVO> gradeResourceList = this.resourceMapper.selectGradeResourceList();
        model.addAttribute("gradeResourceList", gradeResourceList);

        List<GradeVO> gradeList = this.gradeService.retrieveGradeList();
        model.addAttribute("gradeList", gradeList);

        GradeResourceVO gradeResourceVo = new GradeResourceVO();
        model.addAttribute("gradeResourceVo", gradeResourceVo);

        model.addAttribute("managerContent", "/view/security/resourceManager");

        return "view/home/viewManagerTemplate";
    }

    @ResponseBody
    @PostMapping("/selectListAddResource")
    public Map<String, Object> selectListAddResource() throws Exception {
        Map<String, Object> map = new HashMap<>();

        ArrayList<GradeVO> gradeList = this.gradeService.retrieveGradeList();
        map.put("gradeList", gradeList);

        List<ResourceVO> resourceList = this.resourceMapper.selectResourceList();
        map.put("resourceList", resourceList);

        return map;
    }

    //resource + grade 추가
    @ResponseBody
    @PostMapping("/writeResource")
    public Map<String, Object> writeResource(@ModelAttribute("gradeResourceVo") GradeResourceVO gradeResourceVo) {

        Map<String, Object> map = new HashMap<>();

        Map<String, Integer> duplicatedResource = resourceMapper.selectDuplicatedResource(gradeResourceVo.getResourceName(), gradeResourceVo.getGradeNo());
        if(duplicatedResource != null) {
            map.put("duplicated", 1);
        } else {
            map.put("duplicated", 0);
            gradeResourceVo.setResourceName(gradeResourceVo.getResourceName());
            gradeResourceVo.setResourceType(gradeResourceVo.getResourceType());
            gradeResourceVo.setGradeNo(gradeResourceVo.getGradeNo());
            this.resourceMapper.insertGradeResource(gradeResourceVo);
        }

        List<GradeResourceVO> gradeResourceList = this.resourceMapper.selectGradeResourceList();
        map.put("gradeResourceList", gradeResourceList);

        return map;
    }

    //수정 페이지
    @ResponseBody
    @PostMapping("/selectListModifyResource")
    public Map<String, Object> selectListModifyResource(
            @RequestParam("gradeResourceNo") int gradeResourceNo) throws Exception {
        Map<String, Object> map = new HashMap<>();

        ArrayList<GradeVO> gradeList = this.gradeService.retrieveGradeList();
        map.put("gradeList", gradeList);

        List<ResourceVO> resourceList = this.resourceMapper.selectResourceList();
        map.put("resourceList", resourceList);

        GradeResourceVO oneGradeResource = this.resourceMapper.selectOneGradeResource(gradeResourceNo);
        map.put("oneGradeResource", oneGradeResource);

        return map;
    }

    @ResponseBody
    @PostMapping("/modifyResource")
    public Map<String, Object> modifyResource( @ModelAttribute("gradeResourceVo") GradeResourceVO gradeResourceVo) {
        Map<String, Object> map = new HashMap<>();

        Map<String, Integer> duplicatedResource = resourceMapper.selectDuplicatedResource(gradeResourceVo.getResourceName(), gradeResourceVo.getGradeNo());

        if(duplicatedResource == null) { //중복x
            map.put("duplicated", 0);
            log.info("1");
            this.resourceMapper.updateGradeResource(gradeResourceVo);
        } else { //중복o
            if (duplicatedResource.get("resource_no") == gradeResourceVo.getResourceNo()) {
                map.put("duplicated", 0);
                log.info("2");
                this.resourceMapper.updateGradeResource(gradeResourceVo);
            } else {
                map.put("duplicated", 1);
                log.info("3");
            }
        }
        gradeResourceVo = this.resourceMapper.selectOneGradeResource(gradeResourceVo.getGradeResourceNo());
        map.put("gradeResourceVo", gradeResourceVo);
        return map;
    }

    //resource + grade 삭제
    @ResponseBody
    @PostMapping("/deleteResource")
    public Map<String, Object> deleteResource(@ModelAttribute("gradeResourceVo") GradeResourceVO gradeResourceVo) {

        this.resourceMapper.deleteGradeResource(gradeResourceVo.getGradeResourceNo());

        Map<String, Object> map = new HashMap<>();
        List<GradeResourceVO> gradeResourceList = this.resourceMapper.selectGradeResourceList();
        map.put("gradeResourceList", gradeResourceList);

        return map;
    }

}
