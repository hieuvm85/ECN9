package com.example.Ecommerce_BE.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Ecommerce_BE.model.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer>{
}
