package com.example.AsyncFlow.demo.interfaces.controller;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import com.example.AsyncFlow.demo.application.MonitorService;
import com.example.AsyncFlow.demo.infrastructure.aws.sqs.SqsMonitorService;
import com.example.AsyncFlow.demo.infrastructure.persistencia.entity.NotaFiscalMonitorEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Controller do dashboard de monitoramento.
 * Serve tanto a página HTML (Thymeleaf) quanto endpoints REST para a API.
 */
@Controller
@RequiredArgsConstructor
@Tag(name = "Monitor", description = "Dashboard de observabilidade do AsyncFlow")
public class MonitorController {

    private final MonitorService monitorService;

    // ─────────────────────────────────────────────────────────────────────────
    // Dashboard HTML (Thymeleaf)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Página principal do dashboard.
     * Atualiza automaticamente a cada 10 segundos via meta refresh.
     */
    @GetMapping("/")
    public String dashboard(Model model) {
        MonitorService.DashboardData data = monitorService.getDashboardData();
        model.addAttribute("notas", data.notas());
        model.addAttribute("contagem", data.contagemPorStatus());
        model.addAttribute("filas", data.filas().values());
        return "dashboard";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // REST API (para integrações e Swagger)
    // ─────────────────────────────────────────────────────────────────────────

    @GetMapping("/api/monitor/notas")
    @ResponseBody
    @Operation(summary = "Lista todas as notas fiscais", description = "Retorna todas as notas ordenadas por data (mais recentes primeiro)")
    public ResponseEntity<List<NotaFiscalMonitorEntity>> getAllNotas() {
        return ResponseEntity.ok(monitorService.getAllNotas());
    }

    @GetMapping("/api/monitor/notas/status")
    @ResponseBody
    @Operation(summary = "Filtra notas por status", description = "Retorna notas fiscais filtradas pelo status informado")
    public ResponseEntity<List<NotaFiscalMonitorEntity>> getNotasByStatus(
            @RequestParam NotaFiscalStatus status) {
        return ResponseEntity.ok(monitorService.getNotasByStatus(status));
    }

    @GetMapping("/api/monitor/contagem")
    @ResponseBody
    @Operation(summary = "Contagem por status", description = "Retorna a quantidade de notas fiscais agrupadas por status")
    public ResponseEntity<Map<String, Long>> getContagem() {
        return ResponseEntity.ok(monitorService.getContagemPorStatus());
    }

    @GetMapping("/api/monitor/filas")
    @ResponseBody
    @Operation(summary = "Status das filas SQS", description = "Retorna informações das filas SQS (mensagens visíveis, em processamento, atrasadas)")
    public ResponseEntity<Map<String, SqsMonitorService.QueueInfo>> getFilas() {
        return ResponseEntity.ok(monitorService.getQueuesInfo());
    }

    @GetMapping("/api/monitor/dashboard")
    @ResponseBody
    @Operation(summary = "Dados completos do dashboard", description = "Retorna todos os dados do dashboard em um único endpoint")
    public ResponseEntity<MonitorService.DashboardData> getDashboard() {
        return ResponseEntity.ok(monitorService.getDashboardData());
    }
}
