package com.hackathon.backend.locationsservice.Domain.Core;

import com.hackathon.backend.locationsservice.Domain.Core.Base.RegularEntity;
import jakarta.persistence.*;

import java.util.UUID;

@Table(name="users")
@Entity
public class User extends RegularEntity {

}
