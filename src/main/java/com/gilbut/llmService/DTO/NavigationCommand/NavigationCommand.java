package com.gilbut.llmService.DTO.NavigationCommand;

import com.gilbut.llmService.DTO.RosMessageDTO.RosMessageDTO;

import java.util.ArrayList;
import java.util.List;

public class NavigationCommand {
    private final List<RosMessageDTO> route = new ArrayList<>();

    public NavigationCommand() {}

    public void clear() {
        route.clear();
    }

    public boolean isEmpty() {
        return route.isEmpty();
    }

    public void addWaypoint(List<RosMessageDTO> dto) {
        route.addAll(0, dto);
    }

    public void setNewRoute(List<RosMessageDTO> newRoute) {
        route.clear();
        route.addAll(newRoute);
    }

    public List<RosMessageDTO> getRoute() {
        return new ArrayList<>(route);
    }
}
