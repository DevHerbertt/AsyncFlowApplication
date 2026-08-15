package com.example.AsyncFlow.demo.interfaces.controller;

import com.example.AsyncFlow.contracts.NotaFiscalStatus;
import com.example.AsyncFlow.demo.application.MonitorService;
import com.example.AsyncFlow.demo.infrastructure.aws.s3.S3MonitorService;
import com.example.AsyncFlow.demo.infrastructure.aws.sqs.SqsMonitorService;
import com.example.AsyncFlow.demo.infrastructure.persistencia.entity.ClientMonitorEntity;
import com.example.AsyncFlow.demo.infrastructure.persistencia.entity.ItemMonitorEntity;
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

@Controller
@RequiredArgsConstructor
@Tag(name = "Monitor", description = "Dashboard de observabilidade do AsyncFlow")
public class MonitorController {

    private final MonitorService monitorService;

    // ─────────────────────────────────────────────────────────────────────────
    // Dashboard HTML (Thymeleaf)
    // ─────────────────────────────────────────────────────────────────────────

    @GetMapping("/")
    public String dashboard(Model model) {
        MonitorService.DashboardData data = monitorService.getDashboardData();
        model.addAttribute("notas",       data.notas());
        model.addAttribute("contagem",    data.contagemPorStatus());
        model.addAttribute("filas",       data.filas().values());
        model.addAttribute("clientes",    data.clientes());
        model.addAttribute("itens",       data.itens());
        model.addAttribute("arquivosS3",  data.arquivosS3());
        model.addAttribute("bucketName",  data.bucketName());
        return "dashboard";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // REST API
    // ─────────────────────────────────────────────────────────────────────────

    @GetMapping("/api/monitor/notas")
    @ResponseBody
    @Operation(summary = "Lista todas as notas fiscais")
    public ResponseEntity<List<NotaFiscalMonitorEntity>> getAllNotas() {
        return ResponseEntity.ok(monitorService.getAllNotas());
    }

    @GetMapping("/api/monitor/notas/status")
    @ResponseBody
    @Operation(summary = "Filtra notas por status")
    public ResponseEntity<List<NotaFiscalMonitorEntity>> getNotasByStatus(@RequestParam NotaFiscalStatus status) {
        return ResponseEntity.ok(monitorService.getNotasByStatus(status));
    }

    @GetMapping("/api/monitor/contagem")
    @ResponseBody
    @Operation(summary = "Contagem de notas por status")
    public ResponseEntity<Map<String, Long>> getContagem() {
        return ResponseEntity.ok(monitorService.getContagemPorStatus());
    }

    @GetMapping("/api/monitor/filas")
    @ResponseBody
    @Operation(summary = "Status das filas SQS")
    public ResponseEntity<Map<String, SqsMonitorService.QueueInfo>> getFilas() {
        return ResponseEntity.ok(monitorService.getQueuesInfo());
    }

    @GetMapping("/api/monitor/clientes")
    @ResponseBody
    @Operation(summary = "Lista todos os clientes do banco")
    public ResponseEntity<List<ClientMonitorEntity>> getClientes() {
        return ResponseEntity.ok(monitorService.getAllClientes());
    }

    @GetMapping("/api/monitor/itens")
    @ResponseBody
    @Operation(summary = "Lista todos os itens do banco")
    public ResponseEntity<List<ItemMonitorEntity>> getItens() {
        return ResponseEntity.ok(monitorService.getAllItens());
    }

    @GetMapping("/api/monitor/s3")
    @ResponseBody
    @Operation(summary = "Lista arquivos no bucket S3")
    public ResponseEntity<List<S3MonitorService.S3FileInfo>> getS3Files() {
        return ResponseEntity.ok(monitorService.getS3Files());
    }

    @GetMapping("/api/monitor/dashboard")
    @ResponseBody
    @Operation(summary = "Dados completos do dashboard")
    public ResponseEntity<MonitorService.DashboardData> getDashboard() {
        return ResponseEntity.ok(monitorService.getDashboardData());
    }
}
