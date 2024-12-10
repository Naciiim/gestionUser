package org.example.gestion_user.service;

import org.example.gestion_user.config.PasswordGenerator;
import org.example.gestion_user.model.dto.AgentDto;
import org.example.gestion_user.model.entity.Agent;
import org.example.gestion_user.model.mapper.AgentMapper;
import org.example.gestion_user.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AgentService  {

    private final AgentRepository agentRepository;

    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public AgentService(AgentRepository agentRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.agentRepository = agentRepository;

        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public AgentDto createAgent(String lastname, String firstname, String email, String emailConfirmation,
                                String numCin, String address, String phonenumber, String description,
                                String birthdate, Long numLicence, Long numRegCom,
                                byte[] cinRectoPath, byte[] cinVersoPath) {
        //confirmation de l'email
        if (!email.equals(emailConfirmation)) {
            throw new IllegalArgumentException("Email does not match confirmation");
        }
        //confirmation du numero de phonenumber
        phonenumber = "+" + phonenumber;
        if (!phonenumber.matches("^\\+212[6-7][0-9]{8}$")) {
            throw new IllegalArgumentException("Phone number does not match the moroccan form");
        }

        String password= PasswordGenerator.generatePassword();
        String encodedPassword = passwordEncoder.encode(password);

        Agent agent = new Agent();
        agent.setUid(agent.generateUid());
        agent.setPassword(encodedPassword);
        agent.setLastname(lastname);
        agent.setFirstname(firstname);
        agent.setEmail(email);
        agent.setNumCin(numCin);
        agent.setAddress(address);
        agent.setPhonenumber(phonenumber);
        agent.setDescription(description);
        agent.setBirthdate(birthdate);
        agent.setNumLicence(numLicence);
        agent.setNumRegCom(numRegCom);

        try {
            agent.setCinRectoPath(cinRectoPath);
            agent.setCinVersoPath(cinVersoPath);
        } catch (Exception e) {
            throw new RuntimeException("Error saving CIN files", e);
        }
        Agent savedAgent = agentRepository.save(agent);
        emailService.sendPasswordEmail(email, password);

        return AgentMapper.INSTANCE.toDto(savedAgent);

    }

    public List<AgentDto> getAllAgents() {
        return agentRepository.findAll().stream()
                .map(AgentMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }
    public AgentDto updateAgent(Long id, AgentDto updatedAgentDto) {
        Optional<Agent> optionalAgent = agentRepository.findById(id);
        if (optionalAgent.isPresent()) {
            Agent agent = optionalAgent.get();
            agent.setLastname(updatedAgentDto.getLastname());
            agent.setFirstname(updatedAgentDto.getFirstname());
            agent.setEmail(updatedAgentDto.getEmail());
            agent.setNumCin(updatedAgentDto.getNumCin());
            agent.setAddress(updatedAgentDto.getAddress());
            agent.setPhonenumber(updatedAgentDto.getPhonenumber());
            agent.setDescription(updatedAgentDto.getDescription());
            agent.setBirthdate(updatedAgentDto.getBirthdate());
            agent.setNumLicence(updatedAgentDto.getNumLicence());
            agent.setNumRegCom(updatedAgentDto.getNumRegCom());
            agent.setFirstLogin(updatedAgentDto.isFirstLogin());

            Agent updatedAgent = agentRepository.save(agent);
            return AgentMapper.INSTANCE.toDto(updatedAgent);
        } else {
            throw new IllegalArgumentException("Agent not found");
        }
    }
    public Optional<AgentDto> getAgentById(Long id)
    {
        return agentRepository.findById(id)
                .map(AgentMapper.INSTANCE::toDto);
    }
    public void deleteAgent(Long id) {
        agentRepository.deleteById(id);
    }

}
