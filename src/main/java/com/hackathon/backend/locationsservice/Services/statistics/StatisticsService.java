package com.hackathon.backend.locationsservice.Services.statistics;

import com.hackathon.backend.locationsservice.Domain.Core.BarrierlessCriteriaScope.BarrierlessCriteriaCheck;
import com.hackathon.backend.locationsservice.Domain.Core.LocationScope.Location;
import com.hackathon.backend.locationsservice.Domain.Enums.LocationStatusEnum;
import com.hackathon.backend.locationsservice.Repositories.BarrierlessCriteriaScope.BarrierlessCriteriaCheckRepository;
import com.hackathon.backend.locationsservice.Repositories.LocationScope.LocationRepository;
import com.hackathon.backend.locationsservice.Security.DTO.Domain.UserDTO;
import com.hackathon.backend.locationsservice.Security.Repositories.UserRepository;
import com.hackathon.backend.locationsservice.Security.Services.UserService;
import com.hackathon.backend.locationsservice.Security.Services.UserServiceImpl;
import com.hackathon.backend.locationsservice.Services.BarrierlessCriteriaScope.BarrierlessCriteriaCheckService;
import com.hackathon.backend.locationsservice.Services.LocationScope.LocationService;
import com.hackathon.backend.locationsservice.Services.statistics.models.UserStatistics;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StatisticsService {

    private final LocationRepository locationRepository;
    private final UserServiceImpl userService;
    private final BarrierlessCriteriaCheckRepository checkRepository;

    public UserStatistics getUserStatistics() {

        Integer numberAddedLocations = 0;
        Integer numberApprovedLocations = 0;
        Integer numberPendingLocations = 0;

        Integer numberAddedChecks = 0;

        UUID userUuid = getUserId();
        if (userUuid != null) {

            List<Location> locations = locationRepository.findAllByCreatedBy(userUuid);
            numberAddedLocations = locations.size();

            for (Location location : locations) {
                switch (location.getStatus()) {
                    case published -> numberApprovedLocations++;
                    case pending -> numberPendingLocations++;
                }
            }

            List<BarrierlessCriteriaCheck> userChecks = checkRepository.findAllByUser_Id(userUuid);
            numberAddedChecks = userChecks.size();

        }
            return new UserStatistics(numberAddedLocations, numberApprovedLocations, numberPendingLocations,
                    numberAddedChecks);

    }

    private UUID getUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        UUID userId = null;
        boolean isAuthenticated = false;

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
            isAuthenticated = true;
        }
        if (isAuthenticated && username != null) {
            UserDTO user = userService.loadWholeUserByUsername(username);
            userId = user.id();
        }

        return userId;

    }
}
