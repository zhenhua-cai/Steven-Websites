import {Injectable} from '@angular/core';
import {MessageService} from 'primeng/api';
import {Subject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AppService {
  blockScreenEvent = new Subject<boolean>();
  constructor(private messageService: MessageService) {
  }
  showWarningToast(summary: string, detail: string): void {
    this.showToastMsg('warn', summary, detail);
  }

  showInfoToast(summary: string, detail: string): void {
    this.showToastMsg('info', summary, detail);
  }

  showErrorToast(summary: string, detail: string): void {
    this.showToastMsg('error', summary, detail);
  }

  showSuccessToast(summary: string, detail: string): void {
    this.showToastMsg('success', summary, detail);
  }

  showToastMsg(severity: string, summary: string, detail: string): void {
    this.messageService.add({severity, summary, detail});
  }
  blockScreen(): void{
    this.blockScreenEvent.next(true);
  }
  unblockScreen(): void{
    this.blockScreenEvent.next(false);
  }
}
