package cn.edu.bit.ruixin.community.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.bit.ruixin.base.common.exp.CommonResult;
import cn.edu.bit.ruixin.community.service.MeetingService;
import cn.edu.bit.ruixin.community.vo.MeetingInfoVo;
import lombok.Data;

@RestController
@RequestMapping("/meeting")
@CrossOrigin
public class MeetingController {
    @Autowired
    private MeetingService meetingService;

    @GetMapping("/getMeeting")
    public CommonResult<meetingInfoReturnVo> getMeetingById(@RequestParam(name = "id") int meetingid){
        meetingInfoReturnVo mReturnVo = new meetingInfoReturnVo();
        mReturnVo.setMeeting(meetingService.getMeeting(meetingid));
        return CommonResult.ok(meetingInfoReturnVo.class).data(mReturnVo);
    }
    @GetMapping("/getMeeting")
    public CommonResult<meetingListReturnVo> getMeetingByUser(@RequestParam(name = "username") String username){
        meetingListReturnVo mReturnVo = new meetingListReturnVo();
        mReturnVo.setMeetings(meetingService.getMeetingsByUsername(username));
        return CommonResult.ok(meetingListReturnVo.class).data(mReturnVo);
    }

    @PutMapping("/setMeeting")
    public CommonResult<Void> setMeeting(@RequestBody MeetingInfoVo newInfoVo) {
        meetingService.modifyMeeting(newInfoVo);
        return CommonResult.ok(Void.class);
        
    }
    
    @PutMapping("/createMeeting")
    public CommonResult<Void> createMeeting(@RequestBody MeetingInfoVo newInfoVo) {
        meetingService.createMeeting(newInfoVo);
        return CommonResult.ok(Void.class);
        
    }

    @PutMapping("/sign")
    public CommonResult<Void> sign(@RequestParam int meetingid,@RequestParam int userid,@RequestParam String note) {
        meetingService.signToMeeting(meetingid, userid, note);
        return CommonResult.ok(Void.class);
        
    }
}

@Data
class meetingInfoReturnVo{
    private MeetingInfoVo meeting;
}
@Data
class meetingListReturnVo{
    private List<MeetingInfoVo> meetings;
}
