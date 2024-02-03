package com.school.sba.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.sba.entity.ClassHour;
import com.school.sba.entity.User;

@Repository
public interface ClassHourRepository extends JpaRepository<ClassHour, Integer>{
	
	boolean existsByRoomNoAndBeginsAtAndEndsAt(int roomNo, LocalDateTime beginsAt, LocalDateTime endsAt);
	
	
	List<ClassHour> findByUser(User b);

}
