package com.ren.renshomemadefood.content.listeners;

import com.ren.renshomemadefood.content.models.InstructionsResponse;

import java.util.List;

public interface IInstruction {

    void didFetch(List<InstructionsResponse> instructions, String msg);
    void didError(String msg);
}
