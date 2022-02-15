package cn.edu.bit.ruixin.community.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.edu.bit.ruixin.community.domain.AttendenceRecord;
import cn.edu.bit.ruixin.community.domain.Meeting;
import cn.edu.bit.ruixin.community.domain.User;
import cn.edu.bit.ruixin.community.repository.AppointmentRepository;
import cn.edu.bit.ruixin.community.repository.AttendenceRecordRepository;
import cn.edu.bit.ruixin.community.repository.MeetingRepository;
import cn.edu.bit.ruixin.community.repository.UserRepository;
import cn.edu.bit.ruixin.community.service.MeetingService;
import cn.edu.bit.ruixin.community.vo.MeetingInfoVo;

@Service
public class MeetingServiceImpl implements MeetingService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private AttendenceRecordRepository attendenceRecordRepository;

    @Override
    public void createMeeting(MeetingInfoVo meetinginfo) {
        Meeting meeting = new Meeting();
        meeting.setAppointment(appointmentRepository.findAppointmentById(meetinginfo.getId()));
        meeting.setBegin(new Date(meetinginfo.getBegin()));
        meeting.setEnd(new Date(meetinginfo.getEnd()));
        meeting.setDescription(meetinginfo.getDescription());
        meeting.setLauncher(userRepository.findUserByUsername(meetinginfo.getLauncherName()));
        meeting.setName(meetinginfo.getName());
        meetingRepository.save(meeting);
    }

    @Override
    public void modifyMeeting(MeetingInfoVo meetinginfo) {
        Meeting meeting = meetingRepository.getOne(meetinginfo.getId());
        meeting.setAppointment(appointmentRepository.findAppointmentById(meetinginfo.getId()));
        meeting.setBegin(new Date(meetinginfo.getBegin()));
        meeting.setEnd(new Date(meetinginfo.getEnd()));
        meeting.setDescription(meetinginfo.getDescription());
        meeting.setLauncher(userRepository.findUserByUsername(meetinginfo.getLauncherName()));
        meeting.setName(meetinginfo.getName());
        meetingRepository.save(meeting);
        
    }

    @Override
    public void signToMeeting(int meetingid, int userid, String note) {
        Meeting meeting = meetingRepository.getOne(meetingid);
        User user = userRepository.getOne(userid);
        AttendenceRecord attendenceRecord = new AttendenceRecord();
        attendenceRecord.setMeeting(meeting);
        attendenceRecord.setSignDate(new Date());
        attendenceRecord.setUser(user);
        attendenceRecord.setUserNote(note);
        attendenceRecordRepository.save(attendenceRecord);
        
    }

    @Override
    public MeetingInfoVo getMeeting(int meetingid) {
        Meeting meeting = meetingRepository.getOne(meetingid);
        MeetingInfoVo retval = convertMeetingToVo(meeting);
        return retval;
    }

    @Override
    public List<MeetingInfoVo> getMeetingsByUsername(String username) {
        User launcher = userRepository.findUserByUsername(username);
        List<Meeting> meetings = meetingRepository.findMeetingByLauncher(launcher);
        List<MeetingInfoVo> retval = meetings.stream().map(
            (meeting)->{
                return convertMeetingToVo(meeting);
            }
        ).collect(Collectors.toList());
        return retval;
    }

    private MeetingInfoVo convertMeetingToVo(Meeting meeting){
        MeetingInfoVo retval = new MeetingInfoVo();
        retval.setAppointmentId(meeting.getAppointment().getId());
        retval.setBegin(meeting.getBegin().getTime());
        retval.setEnd(meeting.getEnd().getTime());
        retval.setDescription(meeting.getDescription());
        retval.setLauncherName(meeting.getLauncher().getName());
        retval.setName(meeting.getName());
        return retval;
    }

    
}
