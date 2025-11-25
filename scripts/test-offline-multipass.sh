#!/usr/bin/env bash
set -euo pipefail

# Idempotenter Offline-Test für die Assignments http-client-api und completable-future
# mit Multipass. Nutzt das mitgelieferte .mvn/repository für Offline-Builds.
#
# Verhalten:
# 1) Startet (oder nutzt) eine VM (Default: "offline-test").
# 2) Synct das aktuelle Repo in die VM (vorherige Kopie wird ersetzt).
# 3) Kappt das Netz in der VM, ermittelt das Default-Interface automatisch.
# 4) Führt die Maven-Tests offline aus.
# 5) Aktiviert das Netz optional wieder (Default: ja, mit RESTORE_NET=false abschaltbar).
#
# Konfigurierbar per Env:
#   INSTANCE         Name der VM (Default: offline-test)
#   VM_PATH          Zielpfad in der VM (Default: /home/ubuntu/java17-course)
#   RESTORE_NET      true|false ob nach den Tests das Netz wieder aktiviert wird (Default: true)
#   MAVEN_OPTS       Zusätzliche Maven-Optionen
#
# Beispiele:
#   ./scripts/test-offline-multipass.sh
#   RESTORE_NET=false INSTANCE=cf-offline ./scripts/test-offline-multipass.sh

INSTANCE="${INSTANCE:-offline-test}"
VM_PATH="${VM_PATH:-/home/ubuntu/java17-course}"
RESTORE_NET="${RESTORE_NET:-true}"

here="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

echo "==> Sicherstellen, dass VM '${INSTANCE}' läuft..."
if ! multipass info "${INSTANCE}" >/dev/null 2>&1; then
  multipass launch --name "${INSTANCE}" --cpus 2 --memory 4G --disk 10G
else
  if ! multipass list | grep -q "^${INSTANCE}.*Running"; then
    multipass start "${INSTANCE}"
  fi
fi

echo "==> Vorherige Projektkopie in der VM entfernen..."
multipass exec "${INSTANCE}" -- bash -lc "rm -rf '${VM_PATH}'"

echo "==> Projekt in die VM kopieren..."
multipass transfer --recursive "${here}" "${INSTANCE}:${VM_PATH}"

echo "==> Standard-Netzinterface in der VM ermitteln..."
NET_IFACE="$(multipass exec "${INSTANCE}" -- bash -lc "ip route | awk '/default/ {print \$5; exit}'")"
if [[ -z "${NET_IFACE}" ]]; then
  echo "Konnte Interface nicht ermitteln, abbrechen." >&2
  exit 1
fi
echo "    Interface: ${NET_IFACE}"

echo "==> Netzwerk in der VM deaktivieren..."
multipass exec "${INSTANCE}" -- sudo ip link set "${NET_IFACE}" down

echo "==> Offline-Tests ausführen (http-client-api)..."
multipass exec "${INSTANCE}" -- bash -lc "cd '${VM_PATH}' && mvn -o -pl assignments/http-client-api test -Dmaven.repo.local=.mvn/repository ${MAVEN_OPTS:-}"

echo "==> Offline-Tests ausführen (completable-future)..."
multipass exec "${INSTANCE}" -- bash -lc "cd '${VM_PATH}' && mvn -o -pl assignments/completable-future test -Dmaven.repo.local=.mvn/repository ${MAVEN_OPTS:-}"

if [[ "${RESTORE_NET}" == "true" ]]; then
  echo "==> Netzwerk in der VM reaktivieren..."
  multipass exec "${INSTANCE}" -- sudo ip link set "${NET_IFACE}" up
else
  echo "==> Netzwerk bleibt deaktiviert (RESTORE_NET=false)."
fi

echo "==> Fertig."
