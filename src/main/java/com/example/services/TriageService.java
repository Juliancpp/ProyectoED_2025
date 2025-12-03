package com.example.services;

import com.example.TDAs.DoubleHashMap;
import com.example.TDAs.Entry;
import com.example.TDAs.HeapAdaptablePriorityQueue;
import com.example.TDAs.SinglyLinkedList;
import com.example.models.Paciente;
import com.example.models.Priority;
import com.example.models.TriageLevel;

public class TriageService {

    private final HeapAdaptablePriorityQueue<KeyPaciente, Paciente> globalHeap;
    private final HeapAdaptablePriorityQueue<KeyPaciente, Paciente>[] heapsByLevel;
    
    private final DoubleHashMap<Integer, Entry<KeyPaciente, Paciente>> indexGlobal;
    private final DoubleHashMap<String, Entry<KeyPaciente, Paciente>> indexByLevel;

    @SuppressWarnings("unchecked")
    public TriageService() {
        this.globalHeap = new HeapAdaptablePriorityQueue<>();
        TriageLevel[] levels = TriageLevel.values();
        this.heapsByLevel = (HeapAdaptablePriorityQueue<KeyPaciente, Paciente>[]) new HeapAdaptablePriorityQueue[levels.length];
        for (int i = 0; i < levels.length; i++) {
            this.heapsByLevel[i] = new HeapAdaptablePriorityQueue<>();
        }

        this.indexGlobal = new DoubleHashMap<>();
        this.indexByLevel = new DoubleHashMap<>();
    }

    public void addPaciente(Paciente p) {
        KeyPaciente key = KeyPaciente.fromPaciente(p);

        Entry<KeyPaciente, Paciente> ge = globalHeap.insert(key, p);
        indexGlobal.put(p.getId(), ge);

        TriageLevel level = mapPriorityToTriage(p.getPrioridad());
        int li = level.ordinal();
        Entry<KeyPaciente, Paciente> le = heapsByLevel[li].insert(key, p);

        String lvlKey = level.name() + "#" + p.getId();
        indexByLevel.put(lvlKey, le);
    }

    public Paciente pollNext() {
        if (globalHeap.isEmpty()) return null;

        Entry<KeyPaciente, Paciente> ge = globalHeap.removeMin();
        Paciente p = ge.getValue();

        indexGlobal.remove(p.getId());

        TriageLevel level = mapPriorityToTriage(p.getPrioridad());
        String lvlKey = level.name() + "#" + p.getId();
        Entry<KeyPaciente, Paciente> le = indexByLevel.get(lvlKey);

        if (le != null) {
            try {
                heapsByLevel[level.ordinal()].remove(le);
            } catch (Exception e) {

                System.err.println("Advertencia: Inconsistencia en heap de nivel para paciente " + p.getId() + ". Continuando...");

            }
            indexByLevel.remove(lvlKey);
        }
        
        return p;
    }

    @SuppressWarnings("unchecked")
    public SinglyLinkedList<Paciente> snapshotOrdered() {
        int n = globalHeap.size();
        SinglyLinkedList<Paciente> out = new SinglyLinkedList<>();
        if (n == 0) return out;

        Entry<KeyPaciente, Paciente>[] buffer = (Entry<KeyPaciente, Paciente>[]) new Entry[n];
        int idx = 0;

        while (!globalHeap.isEmpty()) {
            Entry<KeyPaciente, Paciente> e = globalHeap.removeMin();
            buffer[idx++] = e;
            out.addLast(e.getValue());
        }

        for (int i = 0; i < idx; i++) {
            Entry<KeyPaciente, Paciente> e = buffer[i];
            Entry<KeyPaciente, Paciente> ne = globalHeap.insert(e.getKey(), e.getValue());
            indexGlobal.put(e.getValue().getId(), ne);
        }

        return out;
    }

    public int size() {
        return globalHeap.size();
    }

    public int countByLevel(TriageLevel level) {
        int li = level.ordinal();
        return heapsByLevel[li].size();
    }

    public boolean removeById(int id) {
        Entry<KeyPaciente, Paciente> ge = indexGlobal.get(id);
        if (ge == null) return false;

        Paciente p = ge.getValue();

        try {
            globalHeap.remove(ge);
        } catch (Exception e) {
            System.err.println("Error removiendo de globalHeap: " + e.getMessage());
            return false;
        }
        indexGlobal.remove(id);

        TriageLevel level = mapPriorityToTriage(p.getPrioridad());
        String lvlKey = level.name() + "#" + id;
        Entry<KeyPaciente, Paciente> le = indexByLevel.get(lvlKey);
        
        if (le != null) {
            try {
                heapsByLevel[level.ordinal()].remove(le);
            } catch (Exception e) {
                System.err.println("Advertencia: Error al remover del nivel (id " + id + ")");
            }
            indexByLevel.remove(lvlKey);
        }
        return true;
    }

    public boolean updatePriority(int id, Priority nuevaPrioridad) {

        Entry<KeyPaciente, Paciente> ge = indexGlobal.get(id);
        if (ge == null) return false;
        Paciente p = ge.getValue();
        removeById(id);
        p.setPrioridad(nuevaPrioridad);
        addPaciente(p);
        return true;
    }

    private TriageLevel mapPriorityToTriage(Priority prioridad) {
        switch (prioridad) {
            case URGENTE:
                return TriageLevel.NIVEL_2_EMERGENCIA;
            case MEDIO:
                return TriageLevel.NIVEL_3_URGENTE;
            case BAJO:
            default:
                return TriageLevel.NIVEL_5_NO_URGENTE;
        }
    }

    private static final class KeyPaciente implements Comparable<KeyPaciente> {
        final int prioridadOrdinal;
        final java.time.LocalDateTime fecha;
        final int id;

        private KeyPaciente(int prioridadOrdinal, java.time.LocalDateTime fecha, int id) {
            this.prioridadOrdinal = prioridadOrdinal;
            this.fecha = fecha;
            this.id = id;
        }

        static KeyPaciente fromPaciente(Paciente p) {
            return new KeyPaciente(p.getPrioridad().ordinal(), p.getFechaIngreso(), p.getId());
        }

        @Override
        public int compareTo(KeyPaciente o) {
            if (this.prioridadOrdinal != o.prioridadOrdinal) {
                return Integer.compare(this.prioridadOrdinal, o.prioridadOrdinal);
            }
            int t = this.fecha.compareTo(o.fecha);
            if (t != 0) return t;
            return Integer.compare(this.id, o.id);
        }
    }

    public Paciente peekNext() {
    if (globalHeap.isEmpty()) return null;
    return globalHeap.min().getValue();
    }
}