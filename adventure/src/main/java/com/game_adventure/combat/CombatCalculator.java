package com.game_adventure.combat;

import com.game_adventure.entity.Entity;
import com.game_adventure.entity.Player;

public class CombatCalculator {

    /**
     * 전투 데미지 계산 및 적용 메서드
     * @param attacker 공격을 주도한 엔티티 (Player가 이동해서 박았으면 Player, Enemy가 이동했으면 Enemy)
     * @param defender 공격을 당한 엔티티
     * @param isDashAttack 대시 공격 여부 (Player 공격 시에만 유효)
     */
    public static void calculateDamage(Entity attacker, Entity defender, boolean isDashAttack) {
        
        // 1. 공격자가 수비자를 때림 (항상 발생)
        // 공격자의 공격력만큼 수비자 HP 감소
        int damageToDefender = attacker.getStats().getAttackPower();
        defender.onAttacked(damageToDefender);

        // 2. 반동 데미지 처리 로직
        // 공격자가 '플레이어'인 경우에만 상황에 따라 반동이 발생함
        if (attacker instanceof Player) {
            if (isDashAttack) {
                // Case A: 대시 공격 -> 플레이어(Attacker)는 데미지를 입지 않음 (무적)
                System.out.println(">> 대시 공격 성공! (플레이어 무피해)");
            } else {
                // Case B: 일반 이동 공격 -> 플레이어(Attacker)도 적의 공격력만큼 데미지를 입음 (쌍방 피해)
                int recoilDamage = defender.getStats().getAttackPower();
                attacker.onAttacked(recoilDamage);
                System.out.println(">> 일반 공격! (플레이어도 반동 피해를 입음)");
            }
        }
        
        // 3. 공격자가 '적(Enemy)'인 경우
        else {
            // Case C: 적이 플레이어에게 박음 -> 플레이어(Defender)만 데미지를 입음
            // 적(Attacker)은 데미지를 입지 않음 (그대로 둠)
        }
    }
}